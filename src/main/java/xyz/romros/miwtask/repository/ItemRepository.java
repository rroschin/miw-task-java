package xyz.romros.miwtask.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import xyz.romros.miwtask.repository.domain.Item;

public interface ItemRepository extends CrudRepository<Item, Integer> {

  String PRICE_SURGE_TIME_INTERVAL_SQL = "INTERVAL 1 HOUR";

  //language=SQL
  String SELECT_ALL_WITH_ADJUSTED_PRICE = ""//
                                          + "SELECT item.id, item.name, item.description, item.quantity, "//
                                          + "(CASE WHEN COUNT(item.id) > 10 THEN item.price * 1.1 ELSE item.price END) AS price "//
                                          + "FROM item "//
                                          + "LEFT JOIN ("//
                                          + "           SELECT item_view_log.item_id "//
                                          + "           FROM item_view_log "//
                                          + "           WHERE item_view_log.view_time > (NOW() - " + PRICE_SURGE_TIME_INTERVAL_SQL + ") "//
                                          + "          ) AS ivl ON item.id = ivl.item_id "//
                                          + "GROUP BY item.id ";
  //language=SQL
  String SELECT_ONE_WITH_ADJUSTED_PRICE = SELECT_ALL_WITH_ADJUSTED_PRICE//
                                          + "HAVING item.id = :itemId";

  @Query(SELECT_ALL_WITH_ADJUSTED_PRICE)
  List<Item> findAllWithAdjustedPrice();

  @Query(SELECT_ONE_WITH_ADJUSTED_PRICE)
  Optional<Item> findByIdWithAdjustedPrice(@Param("itemId") Integer itemId);

}
