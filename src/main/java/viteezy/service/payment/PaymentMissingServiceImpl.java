package viteezy.service.payment;

import com.google.common.base.Throwables;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.db.PaymentPlanRepository;
import viteezy.db.PaymentRepository;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentPlanStatus;
import viteezy.domain.payment.PaymentStatus;
import viteezy.service.CustomerService;
import viteezy.service.mail.EmailService;

import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PaymentMissingServiceImpl implements PaymentMissingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentMissingService.class);

    private final PaymentRepository paymentRepository;
    private final PaymentPlanRepository paymentPlanRepository;
    private final CustomerService customerService;
    private final EmailService emailService;

    protected PaymentMissingServiceImpl(PaymentRepository paymentRepository,
                                        PaymentPlanRepository paymentPlanRepository,
                                        CustomerService customerService,
                                        EmailService emailService) {
        this.paymentRepository = paymentRepository;
        this.paymentPlanRepository = paymentPlanRepository;
        this.customerService = customerService;
        this.emailService = emailService;
    }

    @Override
    public Try<Seq<Long>> missingPaymentsByChargebackDate(Integer attempts) {
        final int daysAgo = attempts == 2 ? 14 : 28;
        final int daysUntil = attempts == 2 ? 15 : 28 + 30;
        return paymentRepository.findByChargebackDate(daysAgo, daysUntil)
                .map(payments -> payments.stream()
                        .filter(this::filterUnpaid)
                        .map(payment -> handlePayment(payment, attempts))
                        .collect(Collectors.toList())
                )
                .flatMap(Try::sequence)
                .onFailure(peekException());
    }

    @Override
    public Try<Seq<Long>> upcomingPaymentsByDate(Integer daysUntil) {
        return paymentPlanRepository.findByPaymentDate(daysUntil, PaymentPlanStatus.ACTIVE)
                .map(paymentPlans -> paymentPlans.stream()
                        .peek(paymentPlan -> LOGGER.info(paymentPlan.paymentMethod()))
                        .map(paymentPlan -> customerService.find(paymentPlan.customerId())
                                .peek(customer -> emailService.sendPendingPayment(customer, paymentPlan))
                                .map(__ -> paymentPlan.id()))
                .collect(Collectors.toList()))
                .flatMap(Try::sequence)
                .onFailure(peekException());
    }

    private boolean filterUnpaid(Payment payment) {
        return !paymentRepository.findByRetriedMolliePaymentId(payment.getMolliePaymentId())
                .map(payments1 -> payments1.stream().anyMatch(payment1 -> payment1.getStatus().equals(PaymentStatus.paid) || payment1.getStatus().equals(PaymentStatus.refund)))
                .getOrElse(false);
    }

    private Try<Long> handlePayment(Payment payment, Integer attempts) {
        LOGGER.debug("handling retry for {}", payment.getMolliePaymentId());
        return paymentPlanRepository.find(payment.getPaymentPlanId())
                .flatMap(paymentPlan -> customerService.find(paymentPlan.customerId()).map(customer -> new Tuple2<>(paymentPlan, customer)))
                .peek(tuple2 -> emailService.sendPaymentMissing(tuple2._2.getFirstName(), tuple2._2.getEmail(), payment, tuple2._1.externalReference(), attempts))
                .map(__ -> payment.getId())
                .onFailure(peekException());
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}
