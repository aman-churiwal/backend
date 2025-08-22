package viteezy.service.payment;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Transactional;
import viteezy.controller.dto.CheckoutPatchRequest;
import viteezy.controller.dto.dashboard.PaymentPlanPatchRequest;
import viteezy.domain.blend.Blend;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.payment.PaymentPlanStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PaymentPlanService {
    Try<PaymentPlan> getPaymentPlan(UUID planExternalReference);

    Try<PaymentPlan> findActivePaymentPlanByBlendExternalReference(UUID blendExternalReference);

    Try<PaymentPlan> findActivePaymentPlanByBlendId(Long blendId);

    Try<PaymentPlan> findPaymentPlanByStatusAndCustomerExternalReference(UUID customerExternalReference, PaymentPlanStatus paymentPlanStatus);

    Try<List<PaymentPlan>> findActivePaymentPlansWithNoPayment(Integer daysAgo);

    Try<List<PaymentPlan>> findPaymentPlans(UUID customerExternalReference);

    @Transactional
    Try<PaymentPlan> changeDeliveryDate(UUID planExternalReference, LocalDateTime deliveryDate);

    @Transactional
    Try<PaymentPlan> reactivate(UUID planExternalReference);

    @Transactional
    Try<PaymentPlan> stop(UUID planExternalReference, String stopReason);

    @Transactional
    Try<PaymentPlan> updateByBlend(UUID blendExternalReference, CheckoutPatchRequest checkoutPatchRequest);

    @Transactional
    Try<PaymentPlan> updateSubscription(Blend blend, PaymentPlan paymentPlan, Integer monthsSubscribed);

    @Transactional
    Try<PaymentPlan> updateStatus(PaymentPlanPatchRequest paymentPlanPatchRequest);

    @Transactional
    Try<PaymentPlan> applyRecurringCoupon(UUID planExternalReference, String couponCode);

    @Transactional
    Try<Void> updatePaymentDateWithNextPaymentDate();

    @Transactional
    Try<Void> updateDeliveryDateWithNextDeliveryDate();

    @Transactional
    Try<Void> updatePaymentDate();

    @Transactional
    Try<Void> updateDeliveryDate();
}
