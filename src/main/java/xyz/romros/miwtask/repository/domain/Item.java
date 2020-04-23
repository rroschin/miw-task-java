package xyz.romros.miwtask.repository.domain;

import lombok.Value;
import org.springframework.data.annotation.Id;

@Value
public class Item {

  @Id
  Integer id;

  String name;
  String description;
  Integer price;

}
