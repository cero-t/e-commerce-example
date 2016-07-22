package ninja.cero.ecommerce.item.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ninja.cero.ecommerce.item.domain.Item;

@RestController
public class ItemController {
	@Autowired
	ItemRepository itemRepository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Item> findAll() {
		return itemRepository.findAll();
	}

	@RequestMapping(value = "/{ids}", method = RequestMethod.GET)
	public Iterable<Item> findByIds(@PathVariable List<Long> ids) {
		return itemRepository.findAll(ids);
	}
}
