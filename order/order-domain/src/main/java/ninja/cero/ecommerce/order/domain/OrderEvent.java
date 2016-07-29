package ninja.cero.ecommerce.order.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderEvent {
	@Id
	@GeneratedValue
	public Long id;

	public Long orderId;

	public EventType eventType;

	public Timestamp eventTime;
}
