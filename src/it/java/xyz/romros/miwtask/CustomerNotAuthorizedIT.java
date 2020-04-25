package xyz.romros.miwtask;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SuppressWarnings("unchecked")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("integration")
public class CustomerNotAuthorizedIT {

  static final String LOCALHOST = "http://localhost:";

  @LocalServerPort
  int port;

  @Autowired
  TestRestTemplate restTemplate;

  @Test
  public void getAllItems_NoJwtTokenProvided_ErrorResponseForbidden() {
    //given
    final String path = "/api/v1/item";

    //when
    final ResponseEntity<Map> response = this.restTemplate.getForEntity(LOCALHOST + port + path, Map.class);

    //then
    assertThat(response).isNotNull();
    assertThat(response.getBody()).isNotNull();
    final Map<String, Object> actual = (Map<String, Object>) response.getBody();
    assertThat(actual.get("path")).isEqualTo(path);
    assertThat(actual.get("error")).isEqualTo("Forbidden");
    assertThat(actual.get("message")).isEqualTo("Access Denied");
  }

  @Test
  public void placeOrder_NoJwtTokenProvided_ErrorResponseForbidden() {
    //given
    final String path = "/api/v1/order";

    //when
    final ResponseEntity<Map> response = this.restTemplate.postForEntity(LOCALHOST + port + path, emptyMap(), Map.class);

    //then
    assertThat(response).isNotNull();
    assertThat(response.getBody()).isNotNull();
    final Map<String, Object> actual = (Map<String, Object>) response.getBody();
    assertThat(actual.get("path")).isEqualTo(path);
    assertThat(actual.get("error")).isEqualTo("Forbidden");
    assertThat(actual.get("message")).isEqualTo("Access Denied");
  }

}
