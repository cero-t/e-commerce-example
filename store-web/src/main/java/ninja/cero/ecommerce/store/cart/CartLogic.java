package ninja.cero.ecommerce.store.cart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ninja.cero.ecommerce.cart.domain.Cart;
import ninja.cero.ecommerce.item.domain.Item;

@Service
public class CartLogic {
	private static final String CART_URL = "http://cart-service";
	private static final String ITEM_URL = "http://item-service";

	@Autowired
	RestTemplate restTemplate;

	public List<CartItem> findCart(String cartId) {
		// Get cart
		if (cartId == null) {
			return null;
		}
		Cart cart = restTemplate.getForObject(CART_URL + "/" + cartId, Cart.class);

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
}
