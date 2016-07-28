package ninja.cero.ecommerce.store.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import ninja.cero.ecommerce.cart.domain.Cart;
import ninja.cero.ecommerce.cart.domain.CartEvent;
import ninja.cero.ecommerce.stock.domain.Stock;
import ninja.cero.ecommerce.store.UserContext;

@RestController
@RequestMapping("/cart")
public class CartController {
	private static final String CART_URL = "http://cart-service";
	private static final String STOCK_URL = "http://stock-service";

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	UserContext userContext;

	@Autowired
	CartLogic cartLogic;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<CartItem> findCart() {
		// Get cart
		if (userContext.cartId == null) {
			return null;
		}
		return cartLogic.findCart(userContext.cartId);
	}

	@RequestMapping(value = "/items", method = RequestMethod.POST)
	public Cart addItem(@RequestBody CartEvent cartEvent) {
		if (userContext.cartId == null) {
			Cart cart = restTemplate.postForObject(CART_URL, null, Cart.class);
			userContext.cartId = cart.cartId;
		}

		ParameterizedTypeReference<List<Stock>> stocksType = new ParameterizedTypeReference<List<Stock>>() {
		};
		Stock stock = restTemplate.exchange(STOCK_URL + "/" + cartEvent.itemId, HttpMethod.GET, null, stocksType)
				.getBody().get(0);
		if (stock.quantity < cartEvent.quantity) {
			throw new RuntimeException("Not enough stock!");
		}

		Cart cart = restTemplate.postForObject(CART_URL + "/" + userContext.cartId, cartEvent, Cart.class);
		return cart;
	}

	@RequestMapping(value = "/items/{itemId}", method = RequestMethod.DELETE)
	public Cart removeItem(@PathVariable String itemId) {
		if (userContext.cartId == null) {
			return null;
		}

		restTemplate.delete(CART_URL + "/" + userContext.cartId + "/items/" + itemId);
		return restTemplate.getForObject(CART_URL + "/" + userContext.cartId, Cart.class);
	}
}
