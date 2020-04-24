package xyz.romros.miwtask.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderRequest {

  Integer itemId;
  Integer quantity;
  Integer amountProvided;

}
