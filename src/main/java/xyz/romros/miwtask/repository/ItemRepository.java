package xyz.romros.miwtask.repository;

import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import xyz.romros.miwtask.repository.domain.Item;

public interface ItemRepository extends CrudRepository<Item, Integer> {

  //language=SQL
  String SELECT_BY_NAME = ""//
                          + "SELECT * "//
                          + "FROM item "//
                          + "WHERE name = :name";

  @Query(SELECT_BY_NAME)
  List<Item> findByName(@Param("name") String name);

}
