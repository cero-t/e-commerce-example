package ninja.cero.ecommerce.store;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class StoreApplicationTest {
	@Test
	public void test1() {
		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

		System.out.println("show catalog");
		System.out.println(restTemplate.getForObject("http://localhost:8080/catalog", Object.class));

		System.out.println("add cart");
		Map<String, Object> cartItem = new HashMap<>();
		cartItem.put("itemId", 1);
		cartItem.put("quantity", 3);
		System.out.println(restTemplate.postForObject("http://localhost:8080/cart/items", cartItem, Object.class));

		System.out.println("show cart");
		System.out.println(restTemplate.getForObject("http://localhost:8080/cart", Object.class));

		System.out.println("checkout");
		Map<String, Object> map = new HashMap<>();
		map.put("name", "Shin Tanimoto");
		map.put("address", "Yokohama Japan");
		map.put("telephone", "+81 123 456 7890");
		map.put("cardNumber", "1234 5678 8765 4321");
		map.put("cardExpire", "12/20");
		map.put("cardName", "Shin Tanimoto");
		System.out.println(restTemplate.postForObject("http://localhost:8080/order", map, Object.class));
	}

	@Test
	public void test2() {
		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

		System.out.println("show catalog");
		System.out.println(restTemplate.getForObject("http://localhost:8080/catalog", Object.class));
		System.out.println(restTemplate.getForObject("http://localhost:8080/catalog", Object.class));
	}

	@Test
	public void test3() {
		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

		System.out.println("show catalog");
		System.out.println(restTemplate.getForObject("http://localhost:8080/catalog", Object.class));
		System.out.println(restTemplate.getForObject("http://localhost:8080/catalog", Object.class));
		System.out.println(restTemplate.getForObject("http://localhost:8080/catalog", Object.class));
		System.out.println(restTemplate.getForObject("http://localhost:8080/catalog", Object.class));

		System.out.println("add cart");
		Map<String, Object> cartItem = new HashMap<>();
		cartItem.put("itemId", 2);
		cartItem.put("quantity", 1);
		System.out.println(restTemplate.postForObject("http://localhost:8080/cart/items", cartItem, Object.class));
}

	@Test
	public void test4() {
		RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

		System.out.println("show catalog");
		System.out.println(restTemplate.getForObject("http://localhost:8080/catalog", Object.class));
		System.out.println(restTemplate.getForObject("http://localhost:8080/catalog", Object.class));
	}
}
