package viteezy.db;

import io.vavr.control.Try;
import viteezy.domain.fulfilment.Order;
import viteezy.domain.fulfilment.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Try<Order> find(Long id);

    Try<Optional<Order>> find(String orderNumber);

    Try<Order> find(UUID externalReference);

    Try<List<Order>> findByStatus(OrderStatus status);

    Try<Order> findByLastDuplicateOrderNumber();

    Try<Order> findLatestByPaymentId(Long paymentId);

    Try<Order> findByTrackTrace(String trackTrace);

    Try<Optional<Order>> findLatestByCustomerId(Long customerId);

    Try<List<Order>> findAllByCustomerId(Long customerId);

    Try<List<Order>> findByTrackTraceNotDelivered();

    Try<List<Order>> findByPharmacistOrderNumber(String pharmacistOrderNumber);

    Try<Order> save(Order order);

    Try<Order> saveCopy(Order order);
}
