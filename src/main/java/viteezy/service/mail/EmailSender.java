package viteezy.service.mail;

import io.vavr.collection.Seq;
import io.vavr.control.Try;
import viteezy.domain.Customer;
import viteezy.domain.payment.Payment;
import viteezy.domain.pricing.ReferralDiscount;
import viteezy.domain.ingredient.Ingredient;
import viteezy.service.mail.domain.CustomerOrder;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EmailSender {

  Try<Void> sendMagicLink(String email, String token);

  Try<Void> sendOrderConfirmation(CustomerOrder customerOrder);

  Try<Void> sendChangeDeliveryDateConfirmation(String email, String firstName, LocalDateTime deliveryDate);

  Try<Void> sendStopConfirmation(String email, String firstName);

  Try<Void> sendReactivationConfirmation(String email, String firstName, LocalDateTime deliveryDate);

  Try<Void> sendPendingPaymentConfirmation(Customer customer, Seq<Ingredient> ingredients);

  Try<Void> sendReferralCodePaid(String email, String firstName, String referralCodeUsedByFirstName, ReferralDiscount referralDiscount);

  Try<Void> sendPharmacistRequest(List<File> pharmacistRequestFiles);

  Try<Void> sendPaymentMissing(String firstName, String email, Payment payment, UUID paymentPlanExternalReference, int attempts);
}
