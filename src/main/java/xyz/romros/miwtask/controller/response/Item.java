package xyz.romros.miwtask.controller.response;

import lombok.Value;

@Value
public class Item {

  Integer id;

  String name;
  String description;
  Integer price;

}
