package viteezy.gateways.facebook;

import org.springframework.scheduling.annotation.Async;
import viteezy.domain.Customer;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.quiz.Quiz;

import java.util.UUID;

public interface FacebookService {

    @Async
    void sendPurchaseEvent(Customer customer, PaymentPlan paymentPlan, boolean first, String molliePaymentId, String facebookClick);

    @Async
    void sendStartQuiz(Quiz quiz, String facebookPixel, String facebookClick, String userAgent, String userIpAddress, String referer);

    @Async
    void sendLeadEvent(Customer customer, UUID quizExternalReference, String facebookClick);
}
