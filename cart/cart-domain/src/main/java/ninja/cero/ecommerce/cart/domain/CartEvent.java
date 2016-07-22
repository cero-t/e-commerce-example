package ninja.cero.ecommerce.cart.domain;

import java.io.Serializable;

public class CartEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	public Long itemId;

	public int quantity;
}
