package viteezy.service.fulfilment;

import io.vavr.collection.Seq;
import io.vavr.control.Try;
import org.springframework.scheduling.annotation.Async;
import viteezy.domain.*;
import viteezy.domain.fulfilment.Order;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.postnl.Shipment;
import viteezy.domain.fulfilment.OrderStatus;
import viteezy.domain.postnl.TrackTrace;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    Try<Optional<Order>> find(String orderNumber);

    Try<Order> find(Long id);

    Try<Order> find(UUID externalReference);

    Try<List<Order>> findAll(UUID customerExternalReference);

    Try<List<Order>> findByStatus(OrderStatus status);

    Try<Optional<TrackTrace>> findTrackAndTraceByByCustomerExternalReference(UUID externalReference);

    Try<List<Order>> findByPharmacistOrderNumber(String pharmacistOrderNumber);

    Try<Order> save(Payment payment, PaymentPlan paymentPlan, Customer customer);

    void update(Order order);

    @Async
    void update(String fileName, OrderStatus orderStatus);

    Try<Order> createDuplicateOrder(UUID orderExternalReference);

    Try<Order> cancelOrder(UUID orderExternalReference);

    Try<Seq<Order>> updateByTrackTrace(List<Shipment> shipments);
}
