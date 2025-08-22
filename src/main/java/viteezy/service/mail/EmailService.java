package viteezy.service.mail;

import io.vavr.control.Try;
import org.springframework.scheduling.annotation.Async;
import viteezy.domain.Customer;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.pricing.ReferralDiscount;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EmailService {

    @Async
    void createOrder(Customer customer, PaymentPlan paymentPlan);

    @Async
    void sendPendingPayment(Customer customer, PaymentPlan paymentPlan);

    @Async
    void sendChangeDeliveryDateConfirmation(String email, String firstName, LocalDateTime deliveryDate);

    @Async
    void sendReactivationConfirmation(String email, String firstName, LocalDateTime deliveryDate);

    @Async
    void sendStopConfirmation(String email, String firstName);

    Try<Void> sendMagicLink(String email, String token);

    @Async
    void sendReferralCodePaid(String referralCodeUsedByFirstName, Customer customerFrom, ReferralDiscount referralDiscount);

    Try<List<File>> sendPharmacistRequest(List<File> pharmacistRequestFiles);

    @Async
    void sendPaymentMissing(String firstName, String email, Payment payment, UUID paymentPlanExternalReference, int attempts);
}
