package ninja.cero.ecommerce.cart.domain;

import java.math.BigDecimal;
import java.sql.Date;

public class CartItem {
	public Long itemId;

	public String name;

	public String author;

	public BigDecimal unitPrice;

	public Date release;

	public String image;

	public Integer quantity;
}
