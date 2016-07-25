package ninja.cero.ecommerce.payment.app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import ninja.cero.ecommerce.payment.domain.Payment;

@Component
public interface PaymentRepository extends CrudRepository<Payment, String> {
}
