package xyz.romros.miwtask;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;

import java.util.Map;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import xyz.romros.miwtask.controller.request.OrderRequest;
import xyz.romros.miwtask.repository.ItemRepository;
import xyz.romros.miwtask.repository.ItemViewLogRepository;
import xyz.romros.miwtask.repository.domain.Item;
import xyz.romros.miwtask.security.controller.JwtRequest;

@SuppressWarnings("unchecked")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("integration")
public class BuyItemsIT {

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
  ItemRepository itemRepository;
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

    final Item item = itemRepository.findById(TEST_ITEM_1_ID).orElse(null);
    item.setQuantity(TEST_ITEM_1_QUANTITY);
    itemRepository.save(item);
  }

  @Test
  public void buyItems_OrderFor1Item_SuccessResponseItemQuantityUpdated() {
    //given
    final Item itemBefore = itemRepository.findById(TEST_ITEM_1_ID).orElse(null);
    assertThat(itemBefore).isNotNull();
    assertThat(itemBefore.getQuantity()).isEqualTo(TEST_ITEM_1_QUANTITY);

    final String path = "/api/v1/order";
    final String uri = LOCALHOST + port + path;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(authToken);
    OrderRequest orderRequest = new OrderRequest();
    orderRequest.setItemId(TEST_ITEM_1_ID);
    orderRequest.setQuantity(1);
    orderRequest.setAmountProvided(TEST_ITEM_1_PRICE);
    HttpEntity<OrderRequest> request = new HttpEntity<>(orderRequest, headers);

    //when
    final ResponseEntity<Map> response = this.restTemplate.exchange(uri, POST, request, Map.class);

    //then
    assertThat(response).isNotNull();
    assertThat(response.getBody()).isNotNull().isNotEmpty();
    final Map<String, Object> actualItem = (Map<String, Object>) response.getBody();
    assertThat(actualItem).isNotNull();
    assertThat(actualItem.get("itemId")).isEqualTo(orderRequest.getItemId());
    assertThat(actualItem.get("itemName")).isEqualTo(TEST_ITEM_1_NAME);
    assertThat(actualItem.get("itemDescription")).isEqualTo(TEST_ITEM_1_DESCRIPTION);
    assertThat(actualItem.get("itemPrice")).isEqualTo(TEST_ITEM_1_PRICE);
    assertThat(actualItem.get("itemQuantity")).isEqualTo(1);

    final Item itemAfter = itemRepository.findById(orderRequest.getItemId()).orElse(null);
    assertThat(itemAfter).isNotNull();
    assertThat(itemAfter.getQuantity()).isEqualTo(TEST_ITEM_1_QUANTITY - orderRequest.getQuantity());
  }

  @Test
  public void buyItems_Order1ItemThenOrder20Items_ErrorResponseNotEnoughItems() {
    //given
    this.buyItems_OrderFor1Item_SuccessResponseItemQuantityUpdated();
    final Item itemBefore = itemRepository.findById(TEST_ITEM_1_ID).orElse(null);
    assertThat(itemBefore).isNotNull();
    assertThat(itemBefore.getQuantity()).isEqualTo(TEST_ITEM_1_QUANTITY - 1);

    final String path = "/api/v1/order";
    final String uri = LOCALHOST + port + path;

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(authToken);
    OrderRequest orderRequest = new OrderRequest();
    orderRequest.setItemId(TEST_ITEM_1_ID);
    orderRequest.setQuantity(TEST_ITEM_1_QUANTITY);
    orderRequest.setAmountProvided(TEST_ITEM_1_PRICE * TEST_ITEM_1_QUANTITY);
    HttpEntity<OrderRequest> request = new HttpEntity<>(orderRequest, headers);

    //when
    final ResponseEntity<Map> response = this.restTemplate.exchange(uri, POST, request, Map.class);

    //then
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNull();

    final Item itemAfter = itemRepository.findById(orderRequest.getItemId()).orElse(null);
    assertThat(itemAfter).isNotNull();
    assertThat(itemAfter.getQuantity()).isEqualTo(TEST_ITEM_1_QUANTITY - 1);
  }

  /*
   * Tests:
   * Create user -> auth -> get list -> get one -> buy -> check updated quantity
   * Create user -> auth -> get list -> get one -> buy, buy, buy, buy (fail)
   * */

}
