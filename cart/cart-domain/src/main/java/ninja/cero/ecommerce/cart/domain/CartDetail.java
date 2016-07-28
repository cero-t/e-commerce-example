package ninja.cero.ecommerce.cart.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import org.springframework.data.annotation.Id;

public class CartDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	public String cartId;

	public Map<Long, CartItem> items;

	public BigDecimal amount;
}
