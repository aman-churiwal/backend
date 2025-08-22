package viteezy.gateways.slack;

import org.springframework.scheduling.annotation.Async;
import viteezy.domain.payment.PaymentPlan;

public interface SlackService {

    @Async
    void notify(PaymentPlan paymentPlan, boolean retriedPayment, boolean testMode);

    @Async
    void notify(Throwable throwable);

    @Async
    void notifyPharmacistRequest(Throwable throwable);
}
