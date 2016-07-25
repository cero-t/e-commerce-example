package ninja.cero.ecommerce.stock.app;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

	@RequestMapping(value = "", method = RequestMethod.POST)
	@Transactional
	public void keepStock(@RequestBody List<Stock> keeps) {
		keeps.stream().forEach(s -> {
			int count = stockRepository.subtractIfPossible(s.itemId, s.quantity);
			if (count == 0) {
				throw new RuntimeException("Not enough stocks.");
			}
		});
	}
}
