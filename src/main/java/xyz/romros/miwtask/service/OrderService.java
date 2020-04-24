package xyz.romros.miwtask.service;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.romros.miwtask.controller.request.OrderRequest;
import xyz.romros.miwtask.controller.response.ItemResponse;
import xyz.romros.miwtask.repository.ItemRepository;
import xyz.romros.miwtask.repository.domain.Item;

@Slf4j
@Service
public class OrderService {

  private final ItemRepository itemRepository;

  @Autowired
  public OrderService(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }

  @Transactional(propagation = REQUIRED)
  public ResponseEntity<ItemResponse> placeOrder(OrderRequest order) {
    final Optional<Item> itemById = itemRepository.findByIdWithAdjustedPrice(order.getItemId());
    if (!itemById.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    final Item item = itemById.get();
    if (item.getQuantity() < order.getQuantity()) {
      log.warn("Tried to place an order for Item: {}, with quantity = {}, but there are not enough Item.", item, order.getQuantity());
      return ResponseEntity.badRequest().build(); //TODO add more details, like a message = Not Enough Items
    }

    int itemTotalCost = order.getQuantity() * item.getPrice();
    if (itemTotalCost > order.getAmountProvided()) {
      log.warn("Tried to place an order for Item: {}, with AmountProvided = {}, but it is not enough to buy (needed = {}).",//
               item, order.getAmountProvided(), itemTotalCost);
      return ResponseEntity.badRequest().build(); //TODO add more details, like a message = Not Enough Money Provided
    }

    //"buy items = decrease item quantity"
    int updatedQuantity = item.getQuantity() - order.getQuantity();

    log.debug("Updating quantity for Item: {}, updated quantity = {}", item, updatedQuantity);
    item.setQuantity(updatedQuantity);
    itemRepository.save(item);

    return ResponseEntity.ok(ItemResponse.from(item));
  }

}
