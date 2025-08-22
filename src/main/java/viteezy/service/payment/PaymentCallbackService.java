package viteezy.service.payment;

import io.vavr.control.Try;
import org.springframework.transaction.annotation.Transactional;
import viteezy.controller.dto.TestCallbackPaymentPostRequest;
import viteezy.domain.payment.PaymentPlan;

public interface PaymentCallbackService {

    Try<PaymentPlan> testProcessCallback(TestCallbackPaymentPostRequest testCallbackPaymentPostRequest);

    Try<PaymentPlan> processCallback(String paymentId);
}
