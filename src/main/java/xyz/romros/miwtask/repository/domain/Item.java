package xyz.romros.miwtask.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item {

  @Id
  Integer id;

  String name;
  String description;
  Integer price;
  Integer quantity;

}
