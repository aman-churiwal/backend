package viteezy.service.mail;

import com.google.common.base.Throwables;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.domain.Customer;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.pricing.ReferralDiscount;
import viteezy.service.blend.BlendIngredientService;
import viteezy.service.mail.domain.CustomerOrder;
import viteezy.service.pricing.ShippingService;
import viteezy.traits.EnforcePresenceTrait;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class EmailServiceImpl implements EmailService, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private static final BigDecimal VAT_SIX_PERCENTAGE_AS_RATIO = new BigDecimal("1.06");
    private static final BigDecimal VAT_NINE_PERCENTAGE_AS_RATIO = new BigDecimal("1.09");

    private final ShippingService shippingService;
    private final EmailSender emailSender;
    private final BlendIngredientService blendIngredientService;

    protected EmailServiceImpl(ShippingService shippingService, EmailSender emailSender,
                            BlendIngredientService blendIngredientService) {
        this.shippingService = shippingService;
        this.emailSender = emailSender;
        this.blendIngredientService = blendIngredientService;
    }

    @Override
    public void createOrder(Customer customer, PaymentPlan paymentPlan) {
        buildCustomerOrder(customer, paymentPlan)
                .flatMap(emailSender::sendOrderConfirmation)
                .onFailure(peekException());
    }

    @Override
    public void sendPendingPayment(Customer customer, PaymentPlan paymentPlan) {
        blendIngredientService.getIngredients(paymentPlan.blendId())
                .flatMap(ingredients -> emailSender.sendPendingPaymentConfirmation(customer, ingredients))
                .onFailure(peekException());
    }

    @Override
    public void sendChangeDeliveryDateConfirmation(String email, String firstName, LocalDateTime deliveryDate) {
        emailSender.sendChangeDeliveryDateConfirmation(email, firstName, deliveryDate)
                .onFailure(peekException());
    }

    @Override
    public void sendReactivationConfirmation(String email, String firstName, LocalDateTime deliveryDate) {
        emailSender.sendReactivationConfirmation(email, firstName, deliveryDate)
                .onFailure(peekException());
    }

    @Override
    public void sendStopConfirmation(String email, String firstName) {
        emailSender.sendStopConfirmation(email, firstName)
                .onFailure(peekException());
    }

    @Override
    public Try<Void> sendMagicLink(String email, String token) {
        return emailSender.sendMagicLink(email, token)
                .onFailure(peekException());
    }

    @Override
    public void sendReferralCodePaid(String referralCodeUsedByFirstName, Customer customerFrom, ReferralDiscount referralDiscount) {
        emailSender.sendReferralCodePaid(customerFrom.getEmail(), customerFrom.getFirstName(), referralCodeUsedByFirstName, referralDiscount)
                .onFailure(peekException());
    }

    @Override
    public Try<List<File>> sendPharmacistRequest(List<File> pharmacistRequestFiles) {
        return emailSender.sendPharmacistRequest(pharmacistRequestFiles)
                .map(__ -> pharmacistRequestFiles)
                .onFailure(peekException());
    }

    @Override
    public void sendPaymentMissing(String firstName, String email, Payment payment, UUID paymentPlanExternalReference, int attempts) {
        emailSender.sendPaymentMissing(firstName, email, payment, paymentPlanExternalReference, attempts)
                .onFailure(peekException());
    }

    private Try<CustomerOrder> buildCustomerOrder(Customer customer, PaymentPlan paymentPlan) {
        final BigDecimal initialAndDiscountedTotal = paymentPlan.firstAmount();
        final BigDecimal recurringAndNotDiscountedAmount = paymentPlan.recurringAmount();
        final BigDecimal subTotal = getSubTotal(recurringAndNotDiscountedAmount, customer.getCountry());
        final BigDecimal taxTotal = recurringAndNotDiscountedAmount.subtract(subTotal);
        final BigDecimal shippingTotal = calculateShippingCost(recurringAndNotDiscountedAmount);
        final BigDecimal discountedTotal = recurringAndNotDiscountedAmount.subtract(initialAndDiscountedTotal);
        return blendIngredientService.getIngredients(paymentPlan.blendId())
                .map(ingredients -> new CustomerOrder(customer, paymentPlan.status(), ingredients, subTotal, discountedTotal, shippingTotal, taxTotal, initialAndDiscountedTotal));
    }

    private BigDecimal getSubTotal(BigDecimal recurringAndNotDiscountedAmount, String country) {
        if ("BE".equals(country)) {
            return recurringAndNotDiscountedAmount.divide(VAT_SIX_PERCENTAGE_AS_RATIO, 2, RoundingMode.HALF_UP);
        } else {
            return recurringAndNotDiscountedAmount.divide(VAT_NINE_PERCENTAGE_AS_RATIO, 2, RoundingMode.HALF_UP);
        }
    }

    private BigDecimal calculateShippingCost(BigDecimal totalAmount) {
        return shippingService.getShippingCostForAmount(totalAmount);
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}
