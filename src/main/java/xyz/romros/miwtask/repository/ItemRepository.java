package xyz.romros.miwtask.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import xyz.romros.miwtask.repository.domain.Item;

public interface ItemRepository extends CrudRepository<Item, Integer> {

  //language=SQL
  String SELECT_ALL_WITH_ADJUSTED_PRICE = ""//
                                          + "SELECT item.id, item.name, item.description, item.quantity, "//
                                          + "(CASE WHEN COUNT(item.id) > 10 THEN item.price * 1.1 ELSE item.price END) AS price "//
                                          + "FROM item "//
                                          + "LEFT JOIN item_view_log ON item.id = item_view_log.item_id "//
                                          + "GROUP BY item.id ";
  //language=SQL
  String SELECT_ONE_WITH_ADJUSTED_PRICE = SELECT_ALL_WITH_ADJUSTED_PRICE//
                                          + "HAVING item.id = :itemId";

  @Query(SELECT_ALL_WITH_ADJUSTED_PRICE)
  List<Item> findAllWithAdjustedPrice();

  @Query(SELECT_ONE_WITH_ADJUSTED_PRICE)
  Optional<Item> findByIdWithAdjustedPrice(@Param("itemId") Integer itemId);

}
