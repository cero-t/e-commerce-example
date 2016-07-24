package ninja.cero.ecommerce.stock.app;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import ninja.cero.ecommerce.stock.domain.Stock;

@Component
public interface StockRepository extends CrudRepository<Stock, Long> {
	@Modifying
	@Query("update Stock s set s.quantity = s.quantity + ?2 where s.id = ?1")
	public int add(Long id, int quantity);

	@Modifying
	@Query("update Stock s set s.quantity = s.quantity - ?2 where s.id = ?1 and s.quantity > ?2")
	public int subtractIfPossible(Long id, int quantity);
}
