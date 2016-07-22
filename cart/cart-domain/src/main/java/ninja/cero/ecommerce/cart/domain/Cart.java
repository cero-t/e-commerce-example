package ninja.cero.ecommerce.cart.domain;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.annotation.Id;

public class Cart implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	public String cartId;

	public Map<Long, Integer> items;
}
