package xyz.romros.miwtask.controller.response;

import lombok.Value;
import xyz.romros.miwtask.repository.domain.Item;

@Value
public class ItemResponse {

  Integer id;

  String name;
  String description;
  Integer price;
  Integer quantity;

  public static ItemResponse from(Item item) {
    return new ItemResponse(item.getId(), item.getName(), item.getDescription(), item.getPrice(), item.getQuantity());
  }

}
