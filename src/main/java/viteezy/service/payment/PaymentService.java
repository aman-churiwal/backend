package viteezy.service.payment;

import be.woutschoovaerts.mollie.data.method.MethodResponse;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import io.vavr.control.Try;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import viteezy.controller.dto.CheckoutPostRequest;
import viteezy.controller.dto.dashboard.PaymentPatchRequest;
import viteezy.domain.payment.Payment;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    Payment buildPayment(Long paymentPlanId, PaymentResponse paymentResponse);

    Try<Payment> getByMolliePaymentId(String molliePaymentId);

    Try<Payment> getRetryPayment(UUID planExternalReference);

    Try<List<Payment>> getPayments(UUID planExternalReference);

    Try<List<Payment>> getByCustomerExternalReference(UUID customerExternalReference);

    Try<List<Payment>> getByBlendExternalReference(UUID blendExternalReference);

    @Cacheable(cacheManager = "dayLivedCacheManager", cacheNames = CacheNames.PAYMENT_METHOD, key = "'paymentMethod_'+#country")
    Try<List<MethodResponse>> getPaymentMethods(String country);

    @Transactional
    Try<PaymentResponse> create(UUID blendExternalReference, CheckoutPostRequest checkoutPostRequest, String facebookClick, String gaCookie);

    @Transactional
    Try<PaymentResponse> createRetryFirstPayment(UUID planExternalReference);

    Try<Payment> save(Payment payment);

    Try<Payment> savePayment(Long paymentPlanId, PaymentResponse paymentResponse);

    Try<Payment> updateStatus(PaymentPatchRequest paymentPatchRequest);
}
