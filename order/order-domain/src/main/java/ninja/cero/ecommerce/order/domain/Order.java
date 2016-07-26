package ninja.cero.ecommerce.order.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	public Long id;

	public String name;
	public String address;
	public String telephone;
	public String mailAddress;

	public String cardNumber;
	public String cardExpire;
	public String cardName;

	public String cartId;
}
