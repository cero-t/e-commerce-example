package ninja.cero.ecommerce.item.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	public Long id;

	public String name;

	public String author;

	public BigDecimal unitPrice;

	public Date release;

	public String image;
}
