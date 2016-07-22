package ninja.cero.ecommerce.stock.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ninja.cero.ecommerce.stock.domain.Stock;

@RestController
public class StockController {
	@Autowired
	StockRepository stockRepository;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Stock> findAll() {
		return stockRepository.findAll();
	}

	@RequestMapping(value = "/{ids}", method = RequestMethod.GET)
	public Iterable<Stock> findByIds(@PathVariable List<Long> ids) {
		return stockRepository.findAll(ids);
	}
}
