package ninja.cero.ecommerce.stock.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Stock implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	public Long itemId;

	public Integer quantity;
}
