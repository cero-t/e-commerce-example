package ninja.cero.ecommerce.store.catalog;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import ninja.cero.ecommerce.item.domain.Item;
import ninja.cero.ecommerce.stock.domain.Stock;

@RestController
@RequestMapping("/catalog")
public class CatalogController {
	private static final String ITEM_URL = "http://item-service";
	private static final String STOCK_URL = "http://stock-service";

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<CatalogItem> findCatalog() {
		// Get items
		ParameterizedTypeReference<List<Item>> itemsType = new ParameterizedTypeReference<List<Item>>() {
		};
		List<Item> items = restTemplate.exchange(ITEM_URL, HttpMethod.GET, null, itemsType).getBody();

		// Get item stocks
		String itemIds = items.stream().map(i -> i.id.toString()).collect(Collectors.joining(","));

		ParameterizedTypeReference<List<Stock>> stocksType = new ParameterizedTypeReference<List<Stock>>() {
		};
		List<Stock> stocks = restTemplate.exchange(STOCK_URL + "/" + itemIds, HttpMethod.GET, null, stocksType)
				.getBody();
		Map<Long, Integer> stockMap = stocks.stream().collect(Collectors.toMap(s -> s.itemId, s -> s.quantity));

		// Filter items by stock
		List<CatalogItem> catalogItems = items.stream().map(item -> {
			CatalogItem catalogItem = new CatalogItem();
			catalogItem.id = item.id;
			catalogItem.name = item.name;
			catalogItem.author = item.author;
			catalogItem.release = item.release;
			catalogItem.unitPrice = item.unitPrice;
			catalogItem.image = item.image;
			catalogItem.inStock = stockMap.getOrDefault(item.id, 0) > 0;
			return catalogItem;
		}).collect(Collectors.toList());

		return catalogItems;
	}
}
