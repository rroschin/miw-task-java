package xyz.romros.miwtask;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import xyz.romros.miwtask.repository.ItemViewLogRepository;
import xyz.romros.miwtask.security.controller.JwtRequest;

@SuppressWarnings("unchecked")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("integration")
public class GetItemsIT {

  static final String LOCALHOST = "http://localhost:";
  static final String CUSTOMER_USERNAME = "cat";
  static final String CUSTOMER_PASSWORD = "cat";

  static final int TEST_ITEM_1_ID = 1;
  static final String TEST_ITEM_1_NAME = "TestItem1";
  static final String TEST_ITEM_1_DESCRIPTION = "TestDescription1";
  static final int TEST_ITEM_1_PRICE = 100;
  static final int TEST_ITEM_1_QUANTITY = 20;

  @LocalServerPort
  int port;

  @Autowired
  TestRestTemplate restTemplate;
  @Autowired
  ItemViewLogRepository itemViewLogRepository;

  String authToken;

  @BeforeEach
  public void beforeTestClass() {
    JwtRequest jwtRequest = new JwtRequest();
    jwtRequest.setUsername(CUSTOMER_USERNAME);
    jwtRequest.setPassword(CUSTOMER_PASSWORD);
    final Map jwtResponse = this.restTemplate.postForObject(LOCALHOST + port + "/authentication", jwtRequest, Map.class);
    authToken = (String) jwtResponse.get("jwttoken");

    itemViewLogRepository.deleteAll();
  }

  @Test
  public void getAllItems_NoParams_SuccessResponseAllItems() {
    //given
    final String path = "/api/v1/item";
    final String uri = LOCALHOST + port + path;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(authToken);
    HttpEntity<Void> request = new HttpEntity<>(headers);

    //when
    final ResponseEntity<List> response = this.restTemplate.exchange(uri, GET, request, List.class);

    //then
    assertThat(response).isNotNull();
    assertThat(response.getBody()).isNotNull().isNotEmpty();
    final Map<String, Object> actualFirstItem = (Map<String, Object>) response.getBody().get(0);
    assertThat(actualFirstItem).isNotNull();
    assertThat(actualFirstItem.get("name")).isEqualTo(TEST_ITEM_1_NAME);
    assertThat(actualFirstItem.get("description")).isEqualTo(TEST_ITEM_1_DESCRIPTION);
    assertThat(actualFirstItem.get("price")).isEqualTo(TEST_ITEM_1_PRICE);
    assertThat(actualFirstItem.get("quantity")).isEqualTo(TEST_ITEM_1_QUANTITY);
  }

  @Test
  public void getOneItem_NoParams_SuccessResponseOneItem() {
    //given
    final String path = "/api/v1/item/" + TEST_ITEM_1_ID;
    final String uri = LOCALHOST + port + path;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(authToken);
    HttpEntity<Void> request = new HttpEntity<>(headers);

    //when
    final ResponseEntity<Map> response = this.restTemplate.exchange(uri, GET, request, Map.class);

    //then
    assertThat(response).isNotNull();
    assertThat(response.getBody()).isNotNull().isNotEmpty();
    final Map<String, Object> actualFirstItem = (Map<String, Object>) response.getBody();
    assertThat(actualFirstItem).isNotNull();
    assertThat(actualFirstItem.get("name")).isEqualTo(TEST_ITEM_1_NAME);
    assertThat(actualFirstItem.get("description")).isEqualTo(TEST_ITEM_1_DESCRIPTION);
    assertThat(actualFirstItem.get("price")).isEqualTo(TEST_ITEM_1_PRICE);
    assertThat(actualFirstItem.get("quantity")).isEqualTo(TEST_ITEM_1_QUANTITY);
    assertThat(actualFirstItem.get("id")).isEqualTo(TEST_ITEM_1_ID);
  }

  @Test
  public void getOneItem10Times_NoParams_SuccessResponseOneItemUpdatedPrice() {
    //given
    final String path = "/api/v1/item/" + TEST_ITEM_1_ID;
    final String uri = LOCALHOST + port + path;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(authToken);
    HttpEntity<Void> request = new HttpEntity<>(headers);

    //when
    IntStream.range(0, 10)//
             .forEach(i -> this.restTemplate.exchange(uri, GET, request, Map.class));
    try {
      TimeUnit.SECONDS.sleep(100);
    } catch (InterruptedException ignored) {}

    this.restTemplate.exchange(uri, GET, request, Map.class);
    final ResponseEntity<Map> response = this.restTemplate.exchange(uri, GET, request, Map.class);

    //then
    assertThat(response).isNotNull();
    assertThat(response.getBody()).isNotNull().isNotEmpty();
    final Map<String, Object> actualFirstItem = (Map<String, Object>) response.getBody();
    assertThat(actualFirstItem).isNotNull();
    assertThat(actualFirstItem.get("price")).isEqualTo((int) (TEST_ITEM_1_PRICE * 1.1));
    assertThat(actualFirstItem.get("name")).isEqualTo(TEST_ITEM_1_NAME);
    assertThat(actualFirstItem.get("description")).isEqualTo(TEST_ITEM_1_DESCRIPTION);
    assertThat(actualFirstItem.get("quantity")).isEqualTo(TEST_ITEM_1_QUANTITY);
    assertThat(actualFirstItem.get("id")).isEqualTo(TEST_ITEM_1_ID);
  }

  @Test
  public void getOneItem10Times_CleanUpViewLogs_SuccessResponseOneItemOriginalPrice() {
    //given
    this.getOneItem10Times_NoParams_SuccessResponseOneItemUpdatedPrice();
    final String path = "/api/v1/item/" + TEST_ITEM_1_ID;
    final String uri = LOCALHOST + port + path;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(authToken);
    HttpEntity<Void> request = new HttpEntity<>(headers);

    itemViewLogRepository.deleteAll();

    //when
    final ResponseEntity<Map> response = this.restTemplate.exchange(uri, GET, request, Map.class);

    //then
    assertThat(response).isNotNull();
    assertThat(response.getBody()).isNotNull().isNotEmpty();
    final Map<String, Object> actualFirstItem = (Map<String, Object>) response.getBody();
    assertThat(actualFirstItem).isNotNull();
    assertThat(actualFirstItem.get("price")).isEqualTo(TEST_ITEM_1_PRICE);
    assertThat(actualFirstItem.get("name")).isEqualTo(TEST_ITEM_1_NAME);
  }

}
