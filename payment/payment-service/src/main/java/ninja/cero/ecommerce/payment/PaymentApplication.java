package ninja.cero.ecommerce.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import ninja.cero.ecommerce.payment.app.PaymentSource;

@SpringCloudApplication
@EnableBinding(PaymentSource.class)
public class PaymentApplication {
	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);
	}
}
