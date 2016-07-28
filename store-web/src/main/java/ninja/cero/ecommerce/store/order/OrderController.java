package ninja.cero.ecommerce.store.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import ninja.cero.ecommerce.order.domain.Order;
import ninja.cero.ecommerce.store.UserContext;

@RestController
@RequestMapping("/order")
public class OrderController {
	private static final String ORDER_URL = "http://order-service";

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	UserContext userContext;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public void checkout(@RequestBody Order order) {
		// Check cart
		if (userContext.cartId == null) {
			throw new RuntimeException("No valid cart!");
		}

		// Save order
		order.cartId = userContext.cartId;
		userContext.cartId = null;
		restTemplate.postForObject(ORDER_URL, order, Long.class);
	}
}
