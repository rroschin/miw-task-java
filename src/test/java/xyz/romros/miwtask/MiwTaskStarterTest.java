package xyz.romros.miwtask;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import xyz.romros.miwtask.controller.ItemController;
import xyz.romros.miwtask.controller.OrderController;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("unit")
class MiwTaskStarterTest {

  @Autowired
  ItemController itemController;
  @Autowired
  OrderController orderController;

  @Test
  public void contexLoads_StartingApplication_ControllersNotNull() {
    assertThat(this.itemController).isNotNull();
    assertThat(this.orderController).isNotNull();
  }

}
