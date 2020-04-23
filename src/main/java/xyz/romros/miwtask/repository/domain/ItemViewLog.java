package xyz.romros.miwtask.repository.domain;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemViewLog {

  @Id
  Integer id;

  Integer itemId;
  Timestamp viewTime;

}
