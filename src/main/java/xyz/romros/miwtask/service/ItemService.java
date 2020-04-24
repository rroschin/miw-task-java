package xyz.romros.miwtask.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.romros.miwtask.controller.response.ItemResponse;
import xyz.romros.miwtask.repository.ItemRepository;
import xyz.romros.miwtask.repository.domain.Item;

@Service
public class ItemService {

  private final ItemRepository itemRepository;
  private final ApplicationContext beans;

  @Autowired
  public ItemService(ItemRepository itemRepository, ApplicationContext applicationContext) {
    this.itemRepository = itemRepository;
    this.beans = applicationContext;
  }

  @Transactional(readOnly = true)
  public ResponseEntity<List<ItemResponse>> findAllItems() {
    return ResponseEntity.ok(itemRepository.findAllWithAdjustedPrice()//
                                           .stream()//
                                           .map(ItemResponse::from)//
                                           .collect(toList()));
  }

  @Transactional(readOnly = true)
  public ResponseEntity<ItemResponse> findOneItem(Integer itemId) {
    final Optional<Item> itemById = itemRepository.findByIdWithAdjustedPrice(itemId);
    if (!itemById.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    final Item item = itemById.get();

    beans.getBean(ItemViewLogger.class).logAsync(item);

    return ResponseEntity.ok(ItemResponse.from(item));
  }

}
