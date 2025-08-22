package viteezy.gateways.googleanalytics;

import org.springframework.scheduling.annotation.Async;
import viteezy.domain.Customer;
import viteezy.domain.payment.PaymentPlan;

public interface GoogleAnalyticsService {

    @Async
    void sendEcommerceTransaction(Customer customer, PaymentPlan paymentPlan, String gaSessionId);
}
