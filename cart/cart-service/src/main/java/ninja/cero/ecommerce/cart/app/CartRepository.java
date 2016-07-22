package ninja.cero.ecommerce.cart.app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import ninja.cero.ecommerce.cart.domain.Cart;

@Component
public interface CartRepository extends CrudRepository<Cart, String> {
}
