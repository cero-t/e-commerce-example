package ninja.cero.ecommerce.stock.app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import ninja.cero.ecommerce.stock.domain.Stock;

@Component
public interface StockRepository extends CrudRepository<Stock, Long> {
}
