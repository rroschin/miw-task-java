package xyz.romros.miwtask.controller;

import static java.util.UUID.randomUUID;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.MDC.MDCCloseable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.romros.miwtask.controller.request.OrderRequest;
import xyz.romros.miwtask.controller.response.OrderResponse;
import xyz.romros.miwtask.service.OrderService;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<OrderResponse> getItems(@RequestBody OrderRequest order) {
    try (MDCCloseable ignored = MDC.putCloseable("logEntry", randomUUID().toString())) {
      log.debug("POST Request to /order with body: {}", order);
      return this.orderService.placeOrder(order);
    }
  }

}
