package xyz.romros.miwtask.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import xyz.romros.miwtask.controller.request.OrderRequest;
import xyz.romros.miwtask.controller.response.OrderResponse;
import xyz.romros.miwtask.repository.ItemRepository;
import xyz.romros.miwtask.repository.domain.Item;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("unit")
class OrderServiceTest {

  static final int TEST_ITEM_1_ID = 1;
  static final String TEST_ITEM_1_NAME = "TestItem1";
  static final String TEST_ITEM_1_DESCRIPTION = "TestDescription1";
  static final int TEST_ITEM_1_PRICE = 100;
  static final int TEST_ITEM_1_QUANTITY = 20;

  @MockBean
  ItemRepository itemRepository;

  @Autowired
  OrderService orderService;

  @Test
  public void placeOrder_Order1Item_SuccessResponse() {
    //given
    OrderRequest orderRequest = new OrderRequest();
    orderRequest.setItemId(TEST_ITEM_1_ID);
    orderRequest.setQuantity(1);
    orderRequest.setAmountProvided(TEST_ITEM_1_PRICE);

    Item item = new Item();
    item.setId(TEST_ITEM_1_ID);
    item.setName(TEST_ITEM_1_NAME);
    item.setDescription(TEST_ITEM_1_DESCRIPTION);
    item.setQuantity(TEST_ITEM_1_QUANTITY);
    item.setPrice(TEST_ITEM_1_PRICE);

    given(itemRepository.findByIdWithAdjustedPrice(TEST_ITEM_1_ID)).willReturn(Optional.of(item));
    given(itemRepository.findById(TEST_ITEM_1_ID)).willReturn(Optional.of(item));
    given(itemRepository.save(item)).willReturn(item);

    //when
    final ResponseEntity<OrderResponse> response = orderService.placeOrder(orderRequest);

    //then
    assertThat(response).isNotNull();
    final OrderResponse actual = response.getBody();
    assertThat(actual).isNotNull();
    assertThat(actual.getItemId()).isEqualTo(orderRequest.getItemId());
    assertThat(actual.getItemQuantity()).isEqualTo(orderRequest.getQuantity());
    assertThat(actual.getItemName()).isEqualTo(item.getName());
    assertThat(actual.getItemDescription()).isEqualTo(item.getDescription());
    assertThat(actual.getItemPrice()).isEqualTo(item.getPrice());

    assertThat(item.getQuantity()).isEqualTo(TEST_ITEM_1_QUANTITY - orderRequest.getQuantity());
  }

}
