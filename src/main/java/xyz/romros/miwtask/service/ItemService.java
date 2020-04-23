package xyz.romros.miwtask.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.romros.miwtask.controller.response.Item;
import xyz.romros.miwtask.repository.ItemRepository;

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
  public ResponseEntity<List<Item>> findAllItems() {
    final Stream<xyz.romros.miwtask.repository.domain.Item> items = StreamSupport.stream(itemRepository.findAll().spliterator(), false);

    return ResponseEntity.ok(items.map(i -> new Item(i.getId(), i.getName(), i.getDescription(), i.getPrice()))//
                                  .collect(toList()));
  }

  @Transactional(readOnly = true)
  public ResponseEntity<Item> findOneItem(Integer itemId) {
    final Optional<xyz.romros.miwtask.repository.domain.Item> item = itemRepository.findById(itemId);
    if (!item.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    final xyz.romros.miwtask.repository.domain.Item i = item.get();

    beans.getBean(ItemViewLogger.class).logAsync(i);

    return ResponseEntity.ok(new Item(i.getId(), i.getName(), i.getDescription(), i.getPrice()));
  }

}
