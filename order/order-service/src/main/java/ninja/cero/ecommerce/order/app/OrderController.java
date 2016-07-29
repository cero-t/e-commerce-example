package ninja.cero.ecommerce.order.app;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import ninja.cero.ecommerce.cart.domain.CartDetail;
import ninja.cero.ecommerce.order.domain.EventType;
import ninja.cero.ecommerce.order.domain.Order;
import ninja.cero.ecommerce.order.domain.OrderEvent;
import ninja.cero.ecommerce.payment.domain.Payment;
import ninja.cero.ecommerce.stock.domain.Stock;

@RestController
public class OrderController {
	private static final String CART_URL = "http://cart-service";
	private static final String STOCK_URL = "http://stock-service";
	private static final String PAYMENT_URL = "http://payment-service";

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderEventRepository orderEventRepository;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public void createOrder(@RequestBody Order order) {
		orderRepository.save(order);

		CartDetail cart = restTemplate.getForObject(CART_URL + "/" + order.cartId + "/detail", CartDetail.class);

		// Keep stock
		List<Stock> keepRequests = cart.items.values().stream().map(i -> {
			Stock stock = new Stock();
			stock.itemId = i.itemId;
			stock.quantity = i.quantity;
			return stock;
		}).collect(Collectors.toList());
		restTemplate.postForObject(STOCK_URL, keepRequests, Void.class);

		// Check card
		Payment payment = new Payment();
		payment.name = order.cardName;
		payment.expire = order.cardExpire;
		payment.cardNumber = order.cardNumber;
		payment.amount = cart.amount;
		restTemplate.postForObject(PAYMENT_URL + "/check", payment, Void.class);

		// Start orderEvent
		OrderEvent event = new OrderEvent();
		event.orderId = order.id;
		event.eventType = EventType.START;

		// Order
		rabbitTemplate.convertAndSend("ec-order", order.id);

		// Payment

		// SendMail

	}

	@RequestMapping(value = "/{orderId}/event", method = RequestMethod.POST)
	public void createEvent(@RequestBody OrderEvent orderEvent) {
		orderEventRepository.save(orderEvent);
	}
}
