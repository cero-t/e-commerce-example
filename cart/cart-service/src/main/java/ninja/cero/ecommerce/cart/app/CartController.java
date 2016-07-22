package ninja.cero.ecommerce.cart.app;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ninja.cero.ecommerce.cart.domain.Cart;
import ninja.cero.ecommerce.cart.domain.CartEvent;

@RestController
public class CartController {
	@Autowired
	CartRepository cartRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Iterable<Cart> findAll() {
		return cartRepository.findAll();
	}

	@RequestMapping(value = "/{cartId}", method = RequestMethod.GET)
	public Cart findCartById(@PathVariable String cartId) {
		return cartRepository.findOne(cartId);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public Cart createCart() {
		Cart cart = new Cart();
		cart.items = new LinkedHashMap<>();
		cartRepository.save(cart);

		return cart;
	}

	@RequestMapping(value = "/{cartId}", method = RequestMethod.POST)
	public Cart addItem(@PathVariable String cartId, @RequestBody CartEvent cartEvent) {
		Cart cart = cartRepository.findOne(cartId);
		cart.items.compute(cartEvent.itemId, (key, old) -> old == null ? cartEvent.quantity : old + cartEvent.quantity);
		cartRepository.save(cart);

		return cart;
	}

	@RequestMapping(value = "/{cartId}/items/{itemId}", method = RequestMethod.DELETE)
	public Cart removeItem(@PathVariable String cartId, @PathVariable Long itemId) {
		Cart cart = cartRepository.findOne(cartId);
		cart.items.remove(itemId);
		cartRepository.save(cart);

		return cart;
	}
}
