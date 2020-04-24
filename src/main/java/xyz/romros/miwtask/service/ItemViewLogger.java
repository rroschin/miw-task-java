package xyz.romros.miwtask.service;

import static java.time.Instant.now;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import xyz.romros.miwtask.repository.ItemViewLogRepository;
import xyz.romros.miwtask.repository.domain.Item;
import xyz.romros.miwtask.repository.domain.ItemViewLog;

@Component
@Scope(SCOPE_PROTOTYPE)
public class ItemViewLogger {

  private final ItemViewLogRepository itemViewLogRepository;

  @Autowired
  public ItemViewLogger(ItemViewLogRepository itemViewLogRepository) {
    this.itemViewLogRepository = itemViewLogRepository;
  }

  @Async
  @Transactional(propagation = REQUIRES_NEW)
  public void logAsync(Item i) {
    ItemViewLog itemViewLog = new ItemViewLog(null, i.getId(), Timestamp.from(now()));
    itemViewLogRepository.save(itemViewLog);
  }

  //TODO add Quartz scheduler to clean up old view logs (=TTL)

}
