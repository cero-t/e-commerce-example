package ninja.cero.ecommerce.order.app;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface OrderSource {
	@Output
	MessageChannel order();
}
