package ninja.cero.ecommerce.payment.app;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface PaymentSource {
	@Input
	MessageChannel order();

	@Output
	SubscribableChannel payment();
}
