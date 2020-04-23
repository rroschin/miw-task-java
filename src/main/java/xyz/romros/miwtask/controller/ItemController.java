package xyz.romros.miwtask.controller;

import static java.util.UUID.randomUUID;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.MDC.MDCCloseable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.romros.miwtask.controller.response.Item;
import xyz.romros.miwtask.service.ItemService;

@Slf4j
@RestController
@RequestMapping("/api/v1/item")
public class ItemController {

  private final ItemService itemService;

  @Autowired
  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<Item>> getItems() {
    try (MDCCloseable ignored = MDC.putCloseable("logEntry", randomUUID().toString())) {
      log.debug("GET Request to /item.");
      return this.itemService.findAllItems();
    }
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Item> getOneItem(@PathVariable("id") Integer itemId) {
    try (MDCCloseable ignored = MDC.putCloseable("logEntry", randomUUID().toString())) {
      log.debug("GET Request to /item/id with id = {}", itemId);
      return this.itemService.findOneItem(itemId);
    }
  }

}
