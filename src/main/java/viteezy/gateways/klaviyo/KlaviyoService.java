package viteezy.gateways.klaviyo;

import io.vavr.control.Try;
import viteezy.controller.dto.klaviyo.ProductGetResponse;
import viteezy.domain.Customer;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.quiz.Quiz;

import java.util.List;
import java.util.Optional;

public interface KlaviyoService {

    Try<List<ProductGetResponse>> getProducts();

    Try<Customer> upsertInitialProfile(Customer customer, Optional<Quiz> quiz);

    Try<Customer> upsertFullProfile(Customer customer, PaymentPlan paymentPlan);

    Try<Void> upsertExistingCustomer(Customer customer, PaymentPlan paymentPlan);

    Try<Void> upsertExistingInitialProfile(Customer customer, Optional<Quiz> quiz);

    void createPurchasePaidEvent(Customer customer, PaymentPlan paymentPlan, Payment payment);

    void createEvent(Customer customer, String eventName, String description, Optional<Long> optionalBlendId);
}
