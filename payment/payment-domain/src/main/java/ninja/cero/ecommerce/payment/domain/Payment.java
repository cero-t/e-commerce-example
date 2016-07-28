package ninja.cero.ecommerce.payment.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	public Long id;

	public String cardNumber;

	public String expire;

	public String name;

	public BigDecimal amount;
}
