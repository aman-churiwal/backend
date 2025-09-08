package viteezy.service;
import viteezy.domain.fulfilment.Order;
import viteezy.domain.Product;
import viteezy.domain.payment.Payment;

public interface TripleWhaleService {
    void sendOrder(Order order, Payment payment);
    void sendProduct(Product product);
}
