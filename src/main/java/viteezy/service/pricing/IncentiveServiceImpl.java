package viteezy.service.pricing;

import com.google.common.base.Throwables;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.db.IncentiveRepository;
import viteezy.domain.pricing.Incentive;
import viteezy.domain.pricing.IncentiveType;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.pricing.IncentiveStatus;
import viteezy.domain.payment.PaymentPlanStatus;
import viteezy.service.payment.PaymentPlanService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class IncentiveServiceImpl implements IncentiveService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IncentiveService.class);
    private static final BigDecimal DISCOUNT = new BigDecimal("0.10");
    private final IncentiveRepository incentiveRepository;
    private final PaymentPlanService paymentPlanService;

    protected IncentiveServiceImpl(IncentiveRepository incentiveRepository, PaymentPlanService paymentPlanService) {
        this.incentiveRepository = incentiveRepository;
        this.paymentPlanService = paymentPlanService;
    }

    @Override
    public Try<Optional<Incentive>> findFromCustomer(Long customerId, IncentiveStatus status, IncentiveType incentiveType) {
        return incentiveRepository.findFromCustomer(customerId, status, incentiveType);
    }

    @Override
    public Try<Incentive> save(UUID planExternalReference, IncentiveType incentiveType) {
        return paymentPlanService.getPaymentPlan(planExternalReference)
                .filter(paymentPlan -> PaymentPlanStatus.ACTIVE.equals(paymentPlan.status()))
                .filter(paymentPlan -> IncentiveType.PAUSED.equals(incentiveType) || noPendingDiscount(paymentPlan))
                .map(paymentPlan -> buildIncentive(paymentPlan.customerId(), incentiveType))
                .flatMap(incentiveRepository::save)
                .onFailure(peekException());
    }

    private boolean noPendingDiscount(PaymentPlan paymentPlan) {
        return findFromCustomer(paymentPlan.customerId(), IncentiveStatus.PENDING, IncentiveType.DISCOUNT)
                .map(Optional::isEmpty)
                .fold(__ -> false, aBoolean -> aBoolean);
    }

    private Incentive buildIncentive(Long customerId, IncentiveType incentiveType) {
        final LocalDateTime now = LocalDateTime.now();
        if (IncentiveType.DISCOUNT.equals(incentiveType)) {
            return new Incentive(null, customerId, DISCOUNT, IncentiveStatus.PENDING, IncentiveType.DISCOUNT, now, now);
        } else {
            return new Incentive(null, customerId, null, IncentiveStatus.COMPLETED, incentiveType, now, now);
        }
    }

    @Override
    public Try<Incentive> updateStatus(Long id, IncentiveStatus status) {
        return incentiveRepository.updateStatus(id, status)
                .onFailure(peekException());
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}
