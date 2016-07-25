package ninja.cero.ecommerce.store.cart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import ninja.cero.ecommerce.item.domain.Item;
import ninja.cero.ecommerce.payment.domain.Payment;
import ninja.cero.ecommerce.stock.domain.Stock;
import ninja.cero.ecommerce.store.UserContext;

@RestController
@RequestMapping("/cart")
public class CartController {
	private static final String CART_URL = "http://cart-service";
	private static final String ITEM_URL = "http://item-service";
	private static final String STOCK_URL = "http://stock-service";
	private static final String PAYMENT_URL = "http://payment-service";

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	UserContext userContext;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<CartItem> findCart() {
		// Get cart
		if (userContext.cartId == null) {
			return null;
		}
		Cart cart = restTemplate.getForObject(CART_URL + "/" + userContext.cartId, Cart.class);

		// Find items in cart and convert to map
		String pathKey = cart.items.keySet().stream().map(id -> id.toString()).collect(Collectors.joining(","));
		ParameterizedTypeReference<List<Item>> type = new ParameterizedTypeReference<List<Item>>() {
		};
		List<Item> items = restTemplate.exchange(ITEM_URL + "/" + pathKey, HttpMethod.GET, null, type).getBody();
		Map<Long, Item> itemMap = items.stream().collect(Collectors.toMap(i -> i.id, i -> i));

		// Resolve cart items
		List<CartItem> cartItems = cart.items.entrySet().stream().map(i -> {
			Item item = itemMap.get(i.getKey());
			if (item == null) {
				return null;
			}

			CartItem cartItem = new CartItem();
			cartItem.id = item.id;
			cartItem.name = item.name;
			cartItem.author = item.author;
			cartItem.release = item.release;
			cartItem.unitPrice = item.unitPrice;
			cartItem.image = item.image;
			cartItem.quantity = i.getValue();
			return cartItem;
		}).collect(Collectors.toList());

		return cartItems;
	}

	@RequestMapping(value = "/items", method = RequestMethod.POST)
	public Cart addItem(@RequestBody CartEvent cartEvent) {
		if (userContext.cartId == null) {
			Cart cart = restTemplate.postForObject(CART_URL, null, Cart.class);
			userContext.cartId = cart.cartId;
		}

		Stock stock = restTemplate.getForObject(STOCK_URL + "/" + cartEvent.itemId, Stock.class);
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

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public void checkout(@RequestBody Payment payment) {
		// Get cart
		if (userContext.cartId == null) {
			throw new RuntimeException("No valid cart!");
		}
		Cart cart = restTemplate.getForObject(CART_URL + "/" + userContext.cartId, Cart.class);

		// Keep stock
		List<Stock> keepRequests = cart.items.entrySet().stream().map(e -> {
			Stock stock = new Stock();
			stock.itemId = e.getKey();
			stock.quantity = e.getValue();
			return stock;
		}).collect(Collectors.toList());
		restTemplate.postForObject(STOCK_URL, keepRequests, Void.class);

		// Card check
		restTemplate.postForObject(PAYMENT_URL + "/check", payment, Void.class);

		// Order
		// Payment

		userContext.cartId = null;
	}
}
