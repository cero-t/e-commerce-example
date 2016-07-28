package ninja.cero.ecommerce.order.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ninja.cero.ecommerce.order.domain.Order;

@RestController
public class OrderController {
	@Autowired
	OrderRepository orderRepository;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public Long createOrder(@RequestBody Order order) {
		orderRepository.save(order);
		return order.id;
	}
}
