package ninja.cero.ecommerce.item.app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import ninja.cero.ecommerce.item.domain.Item;

@Component
public interface ItemRepository extends CrudRepository<Item, Long> {
}
