package ninja.cero.ecommerce.store.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/catalog")
public class OrderController {
	private static final String ITEM_URL = "http://item-service";
	private static final String STOCK_URL = "http://stock-service";

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public void findCatalog() {
	}
}
