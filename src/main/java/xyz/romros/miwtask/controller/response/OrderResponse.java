package xyz.romros.miwtask.controller.response;

import lombok.Value;

@Value
public class OrderResponse {

  Integer itemId;
  String itemName;
  String itemDescription;
  Integer itemPrice;
  Integer itemQuantity;

}
