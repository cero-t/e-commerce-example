package ninja.cero.ecommerce.payment.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ninja.cero.ecommerce.payment.domain.Payment;

@RestController
public class PaymentController {
	@Autowired
	PaymentRepository paymentRepository;

	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public void check(@RequestBody Payment payment) {
		// Do nothing.
	}

	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public void payment(@RequestBody Payment payment) {
		paymentRepository.save(payment);
	}
}
