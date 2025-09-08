package viteezy.service.fulfilment;

import com.google.common.base.Throwables;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.checkdigit.EAN13CheckDigit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import viteezy.db.OrderRepository;
import viteezy.domain.*;
import viteezy.domain.fulfilment.Order;
import viteezy.domain.klaviyo.KlaviyoConstant;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.postnl.Shipment;
import viteezy.domain.fulfilment.OrderStatus;
import viteezy.domain.postnl.TrackTrace;
import viteezy.gateways.infobip.InfobipService;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.service.CustomerService;
import viteezy.service.TripleWhaleService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final InfobipService infobipService;
    private final KlaviyoService klaviyoService;
    private final TripleWhaleService tripleWhaleService;

    protected OrderServiceImpl(OrderRepository orderRepository, CustomerService customerService,
                               InfobipService infobipService, KlaviyoService klaviyoService, TripleWhaleService tripleWhaleService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.infobipService = infobipService;
        this.klaviyoService = klaviyoService;
        this.tripleWhaleService = tripleWhaleService;
    }

    @Override
    public Try<Optional<Order>> find(String orderNumber) {
        return orderRepository.find(orderNumber);
    }

    @Override
    public Try<Order> find(Long id) {
        return orderRepository.find(id);
    }

    @Override
    public Try<Order> find(UUID externalReference) {
        return orderRepository.find(externalReference);
    }

    @Override
    public Try<List<Order>> findAll(UUID customerExternalReference) {
        return customerService.find(customerExternalReference)
                .flatMap(customer -> orderRepository.findAllByCustomerId(customer.getId()));
    }

    @Override
    public Try<List<Order>> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public Try<Optional<TrackTrace>> findTrackAndTraceByByCustomerExternalReference(UUID externalReference) {
        return customerService.find(externalReference)
                .flatMap(customer -> orderRepository.findLatestByCustomerId(customer.getId()))
                .map(this::buildTrackTraceLink)
                .onFailure(peekException());
    }

    @Override
    public Try<List<Order>> findByPharmacistOrderNumber(String pharmacistOrderNumber) {
        return orderRepository.findByPharmacistOrderNumber(pharmacistOrderNumber);
    }

    @Override
    public Try<Order> save(Payment payment, PaymentPlan paymentPlan, Customer customer) {
        return buildOrder(payment, paymentPlan, customer)
                .flatMap(orderRepository::save)
                .onSuccess(savedOrder -> tripleWhaleService.sendOrder(savedOrder, payment)) // Send order to Triple Whale
                .onFailure(peekException());
    }

    @Override
    public void update(String fileName, OrderStatus orderStatus) {
        Try.of(() -> Long.valueOf(fileName.substring(0, fileName.length() - 4)))
                .flatMap(orderRepository::find)
                .flatMap(order -> orderRepository.save(buildOrder(order, orderStatus)))
                .onFailure(peekException());
    }

    @Override
    public Try<Order> createDuplicateOrder(UUID orderExternalReference) {
        return orderRepository.find(orderExternalReference)
                .filter(order -> filterCreatedOrderByPaymentId(order.getPaymentId()), () -> new DuplicateKeyException("Created duplicate order already exist for this order"))
                .flatMap(order -> customerService.find(order.getCustomerId()).map(customer -> new Tuple2<>(order, customer)))
                .flatMap(orderCustomerTuple -> orderRepository.findByLastDuplicateOrderNumber()
                        .flatMap(orderLowestOrderNumber -> buildDuplicateOrder(orderLowestOrderNumber.getOrderNumber(), orderCustomerTuple._1, orderCustomerTuple._2)))
                .flatMap(orderRepository::saveCopy)
                .onFailure(peekException());
    }

    @Override
    public Try<Order> cancelOrder(UUID orderExternalReference) {
        return orderRepository.find(orderExternalReference)
                .filter(order -> OrderStatus.CREATED.equals(order.getStatus())
                        || OrderStatus.PACKING_SLIP_READY.equals(order.getStatus())
                        || OrderStatus.SHIPPED_TO_PHARMACIST.equals(order.getStatus()))
                .flatMap(order -> orderRepository.save(buildOrder(order, OrderStatus.CANCELED)));
    }

    @Override
    public Try<Seq<Order>> updateByTrackTrace(List<Shipment> shipments) {
        return Try.success(shipments.stream()
                .map(shipment -> orderRepository.findByTrackTrace(shipment.getBarcode())
                        .map(order -> new Tuple2<>(order, getShipmentStatusByShipmentCode(shipment)))
                        .filter(tuple2 -> !tuple2._1.getStatus().equals(tuple2._2))
                        .peek(tuple2 -> checkOrderAtPickupLocation(tuple2._1, tuple2._2))
                        .peek(tuple2 -> checkOrderDelivered(tuple2._1, tuple2._2))
                        .flatMap(tuple2 -> orderRepository.save(buildOrder(tuple2._1, tuple2._2))))
                .collect(Collectors.toList()))
                .flatMap(Try::sequence);
    }

    @Override
    public void update(Order order) {
        orderRepository.save(order)
                .onFailure(peekException());
    }

    private boolean filterCreatedOrderByPaymentId(Long paymentId) {
        return orderRepository.findLatestByPaymentId(paymentId)
                .map(order -> !OrderStatus.CREATED.equals(order.getStatus()) && !OrderStatus.PACKING_SLIP_READY.equals(order.getStatus()))
                .getOrElse(false);
    }

    private Optional<TrackTrace> buildTrackTraceLink(Optional<Order> order) {
        return order
                .filter(value -> value.getTrackTraceCode() != null)
                .map(value -> new TrackTrace(URI.create("https://jouw.postnl.nl/track-and-trace/".concat(value.getTrackTraceCode()).concat("-").concat(value.getShipToCountryCode()).concat("-").concat(value.getShipToPostalCode()))));
    }

    private Order buildOrder(Order order, OrderStatus orderStatus) {
        LocalDateTime shipped = EnumSet.of(OrderStatus.SHIPPED_TO_PHARMACIST, OrderStatus.SHIPPED_TO_POSTNL, OrderStatus.SHIPPED_TO_CUSTOMER).contains(orderStatus) ? LocalDateTime.now() : order.getShipped();
        return new Order(order.getId(), order.getExternalReference(), order.getOrderNumber(), order.getPaymentId(),
                order.getSequenceType(), order.getPaymentPlanId(), order.getBlendId(), order.getCustomerId(),
                order.getRecurringMonths(), order.getShipToFirstName(), order.getShipToLastName(),
                order.getShipToStreet(), order.getShipToHouseNo(), order.getShipToAnnex(), order.getShipToPostalCode(),
                order.getShipToCity(), order.getShipToCountryCode(), order.getShipToPhone(), order.getShipToEmail(),
                order.getReferralCode(), order.getTrackTraceCode(), order.getPharmacistOrderNumber(), orderStatus,
                order.getCreated(), shipped, order.getLastModified());
    }

    private Try<Order> buildOrder(Payment payment, PaymentPlan paymentPlan, Customer customer) {
        return buildOrderNo(payment.getId())
                .map(orderNo -> new Order(null, UUID.randomUUID(), orderNo, payment.getId(),
                payment.getSequenceType(), paymentPlan.id(), paymentPlan.blendId(), customer.getId(),
                paymentPlan.recurringMonths(), customer.getFirstName(), customer.getLastName(), customer.getStreet(),
                customer.getHouseNumber().toString(), customer.getHouseNumberAddition(), customer.getPostcode(),
                customer.getCity(), customer.getCountry(), processPhoneNumber(customer.getPhoneNumber()), customer.getEmail(),
                customer.getReferralCode(), null, null, OrderStatus.CREATED, null, null, null));
    }

    private void checkOrderAtPickupLocation(Order order, OrderStatus shipmentStatus) {
        if (OrderStatus.SHIPMENT_AT_PICK_UP_LOCATION.equals(shipmentStatus)
                && !OrderStatus.SHIPMENT_AT_PICK_UP_LOCATION.equals(order.getStatus())) {
            infobipService.sendOrderAtPickUpLocation(order);
        }
    }

    private void checkOrderDelivered(Order order, OrderStatus shipmentStatus) {
        if (OrderStatus.SHIPMENT_DELIVERED.equals(shipmentStatus) && !OrderStatus.SHIPMENT_DELIVERED.equals(order.getStatus())) {
            customerService.find(order.getCustomerId())
                    .peek(customer -> klaviyoService.createEvent(customer, KlaviyoConstant.SHIPMENT_DELIVERED, null, Optional.of(order.getBlendId())));
        }
    }

    private OrderStatus getShipmentStatusByShipmentCode(Shipment shipment) {
        switch (shipment.getStatus().getStatusCode()) {
            case 1: return OrderStatus.SHIPMENT_PRE_ALERTED;
            case 2: return OrderStatus.SHIPMENT_ACCEPTED;
            case 3: return OrderStatus.SHIPMENT_COLLECTED;
            case 4: return OrderStatus.SHIPMENT_NOT_COLLECTED;
            case 5: return OrderStatus.SHIPMENT_SORTED;
            case 6: return OrderStatus.SHIPMENT_NOT_SORTED;
            case 7: return OrderStatus.SHIPMENT_OUT_FOR_DELIVERY;
            case 8: return OrderStatus.SHIPMENT_NOT_DELIVERED;
            case 9: return OrderStatus.SHIPMENT_AT_CUSTOMS;
            case 11: return OrderStatus.SHIPMENT_DELIVERED;
            case 12: return OrderStatus.SHIPMENT_AT_PICK_UP_LOCATION;
            case 13: return OrderStatus.PRE_ALERTED_SHIPMENT;
            case 14: return OrderStatus.SHIPMENT_PRE_ALERTED;
            case 15: return OrderStatus.MISSING_COLLECTION;
            case 16: return OrderStatus.MISSING_IN_SORTING;
            case 17: return OrderStatus.MISSING_IN_DISTRIBUTION;
            case 18: return OrderStatus.DEFINITELY_MISSING;
            case 19: return OrderStatus.SHIPMENT_REJECTED;
            case 20: return OrderStatus.SHIPMENT_IN_CLEARANCE_PROCESS;
            case 21: return OrderStatus.SHIPMENT_IN_STORAGE;
            case 22: return OrderStatus.SHIPMENT_PICKED_UP;
            case 23: return OrderStatus.SHIPMENT_COLLECTED_BY_POSTNL;
            default:
                LOGGER.warn("Unknown postNL statusCode shipment={}", shipment);
                return OrderStatus.SHIPPED_TO_CUSTOMER;
        }
    }

    private Try<Order> buildDuplicateOrder(String orderNumber, Order order, Customer customer) {
        return buildDuplicateOrderNumber(orderNumber)
                .map(orderNo -> new Order(null, UUID.randomUUID(), orderNo, order.getPaymentId(),
                order.getSequenceType(), order.getPaymentPlanId(), order.getBlendId(), customer.getId(),
                order.getRecurringMonths(), customer.getFirstName(), customer.getLastName(), customer.getStreet(),
                customer.getHouseNumber().toString(), customer.getHouseNumberAddition(), customer.getPostcode(),
                customer.getCity(), customer.getCountry(), processPhoneNumber(customer.getPhoneNumber()), customer.getEmail(),
                customer.getReferralCode(), null, null, OrderStatus.CREATED, null, null, null));
    }

    private Try<String> buildOrderNo(Long paymentId) {
        return Try.of(() -> {
            final EAN13CheckDigit ean13CheckDigit = new EAN13CheckDigit();
            final String leftPad = StringUtils.leftPad(String.valueOf(paymentId), 9, '0');
            final String calculated = ean13CheckDigit.calculate(leftPad);
            return leftPad + calculated;
        });
    }

    private Try<String> buildDuplicateOrderNumber(String orderNumber) {
        return Try.of(() -> Long.parseLong(StringUtils.chop(orderNumber)) + 1)
                .flatMap(this::buildOrderNo);
    }

    private String processPhoneNumber(String phoneNumber) {
        String phoneNumberReplaced = phoneNumber
                .replaceAll("\\s","")
                .replaceAll("NL\\|", "")
                .replaceAll("\\+", "");
        return phoneNumberReplaced.substring(0, Math.min(13, phoneNumberReplaced.length()));
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}
