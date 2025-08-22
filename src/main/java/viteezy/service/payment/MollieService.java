package viteezy.service.payment;

import be.woutschoovaerts.mollie.data.customer.CustomerResponse;
import be.woutschoovaerts.mollie.data.method.MethodResponse;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import io.vavr.control.Try;
import viteezy.domain.Customer;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentPlan;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MollieService {
    Try<CustomerResponse> createCustomer(String email);

    Try<CustomerResponse> updateCustomer(Customer customer);

    Try<PaymentResponse> createCustomerPayment(Customer customer, BigDecimal amount, String description, UUID externalReference, Optional<Long> referralId, String method, String facebookClick, String gaCookie);

    Try<PaymentResponse> createRecurringPayment(Customer customer, BigDecimal amount, String description, UUID externalReference);

    Try<PaymentResponse> find(String paymentId);

    Try<PaymentResponse> createRetryFirstPayment(Customer customer, PaymentPlan paymentPlan, Payment payment);

    Try<List<MethodResponse>> getPaymentMethods(String country);
}
