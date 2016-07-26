package ninja.cero.ecommerce.store.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import ninja.cero.ecommerce.order.domain.Order;
import ninja.cero.ecommerce.payment.domain.Payment;
import ninja.cero.ecommerce.stock.domain.Stock;
import ninja.cero.ecommerce.store.UserContext;
import ninja.cero.ecommerce.store.cart.CartItem;
import ninja.cero.ecommerce.store.cart.CartLogic;

@RestController
@RequestMapping("/order")
public class OrderController {
	private static final String ORDER_URL = "http://order-service";
	private static final String STOCK_URL = "http://stock-service";
	private static final String PAYMENT_URL = "http://payment-service";

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	UserContext userContext;

	@Autowired
	CartLogic cartLogic;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public void checkout(@RequestBody Order order) {
		// Check cart
		if (userContext.cartId == null) {
			throw new RuntimeException("No valid cart!");
		}
		List<CartItem> cartItems = cartLogic.findCart(userContext.cartId);
		if (cartItems == null || cartItems.isEmpty()) {
			throw new RuntimeException("No item in your cart!");
		}

		// Save order
		order.cartId = userContext.cartId;
		userContext.cartId = null;
		Long orderId = restTemplate.postForObject(ORDER_URL, order, Long.class);
		order.id = orderId;

		// Keep stock
		List<Stock> keepRequests = cartItems.stream().map(i -> {
			Stock stock = new Stock();
			stock.itemId = i.id;
			stock.quantity = i.quantity;
			return stock;
		}).collect(Collectors.toList());
		restTemplate.postForObject(STOCK_URL, keepRequests, Void.class);

		// Check card
		BigDecimal sum = cartItems.stream().map(i -> i.unitPrice.multiply(new BigDecimal(i.quantity)))
				.reduce((b1, b2) -> b1.add(b2)).orElse(BigDecimal.ZERO);

		Payment payment = new Payment();
		payment.name = order.cardName;
		payment.expire = order.cardExpire;
		payment.cardNumber = order.cardNumber;
		payment.amount = sum;
		restTemplate.postForObject(PAYMENT_URL + "/check", payment, Void.class);

		// Order

		// Payment

		// SendMail

	}
}
