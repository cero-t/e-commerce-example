package ninja.cero.ecommerce.cart.app;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
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
import ninja.cero.ecommerce.cart.domain.CartDetail;
import ninja.cero.ecommerce.cart.domain.CartEvent;
import ninja.cero.ecommerce.cart.domain.CartItem;
import ninja.cero.ecommerce.item.domain.Item;

@RestController
public class CartController {
	private static final String ITEM_URL = "http://item-service";

	@Autowired
	CartRepository cartRepository;

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Iterable<Cart> findAll() {
		return cartRepository.findAll();
	}

	@RequestMapping(value = "/{cartId}", method = RequestMethod.GET)
	public Cart findCartById(@PathVariable String cartId) {
		return cartRepository.findOne(cartId);
	}

	@RequestMapping(value = "/{cartId}/detail", method = RequestMethod.GET)
	public CartDetail findCartDetailById(@PathVariable String cartId) {
		// Create cart
		Cart cart = cartRepository.findOne(cartId);
		if (cart == null) {
			throw new RuntimeException("Cart not found");
		}
		CartDetail cartDetail = new CartDetail();
		cartDetail.cartId = cart.cartId;

		// Find items in cart and convert to map
		String pathKey = cart.items.keySet().stream().map(id -> id.toString()).collect(Collectors.joining(","));
		ParameterizedTypeReference<List<Item>> type = new ParameterizedTypeReference<List<Item>>() {
		};
		List<Item> items = restTemplate.exchange(ITEM_URL + "/" + pathKey, HttpMethod.GET, null, type).getBody();
		Map<Long, Item> itemMap = items.stream().collect(Collectors.toMap(i -> i.id, i -> i));

		// Resolve cart items
		cartDetail.items = cart.items.entrySet().stream().map(i -> {
			Item item = itemMap.get(i.getKey());
			if (item == null) {
				return null;
			}

			CartItem cartItem = new CartItem();
			cartItem.itemId = item.id;
			cartItem.name = item.name;
			cartItem.author = item.author;
			cartItem.release = item.release;
			cartItem.unitPrice = item.unitPrice;
			cartItem.image = item.image;
			cartItem.quantity = i.getValue();
			return cartItem;
		}).collect(Collectors.toMap(i -> i.itemId, i -> i));

		// Count amount
		cartDetail.amount = cartDetail.items.values().stream()
				.map(i -> i.unitPrice.multiply(new BigDecimal(i.quantity))).reduce((b1, b2) -> b1.add(b2))
				.orElse(BigDecimal.ZERO);

		return cartDetail;
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
