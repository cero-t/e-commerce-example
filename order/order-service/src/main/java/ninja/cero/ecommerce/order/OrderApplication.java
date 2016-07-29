package ninja.cero.ecommerce.order;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import ninja.cero.ecommerce.order.app.OrderSource;

@SpringCloudApplication
@EnableBinding(OrderSource.class)
public class OrderApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}
}
