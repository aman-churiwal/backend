package viteezy.service.mail;

import io.vavr.collection.Seq;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import viteezy.configuration.mail.MailConfiguration;
import viteezy.domain.Customer;
import viteezy.domain.payment.*;
import viteezy.domain.pricing.ReferralDiscount;
import viteezy.domain.ingredient.Ingredient;
import viteezy.gateways.slack.SlackService;
import viteezy.service.mail.domain.CustomerOrder;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class EmailSenderImpl implements EmailSender {

  private final SpringTemplateEngine springTemplateEngine;
  private final JavaMailSender javaMailSender;
  private final SlackService slackService;
  private final String frontendBaseUrl;
  private final String defaultOrderBcc;
  private final String pharmacistCsvRecipient;
  private final String pharmacistCsvSubject;

  private static final String SENDER = "Viteezy <info@viteezy.nl>";
  private static final String VITEEZY_LOGO = "/images/viteezy_logo.png";
  private static final String VITEEZY_LOGO_CONTENT_ID = "viteezy_logo";

  private static final String SUBJECT_MAGIC_LINK = "Login-link om in te loggen op Viteezy";
  private static final String MAGIC_LINK_URL = "%s/viteezy/api/login/%s?email=%s";

  private static final String SUBJECT_SECOND_GENERIC_REASON = "Openstaande betaling - herinnering Viteezy";
  private static final String SUBJECT_THIRD_CHARGEBACK_REASON = "Voorkom extra incassokosten";
  private static final String SUBJECT_THIRD_FAILED_REASON = "We willen niet dat je zonder Viteezy komt te zitten. Onderneem actie";

  private static final String SUBJECT_ORDER_CONFIRMATION = "Jouw Viteezy";
  private static final Integer ROUNDING_MODE_DIGITS = 2;

  private static final String SUBJECT_CHANGE_DELIVERY_DATE = "Levering aangepast";

  private static final String SUBJECT_STOP_CONFIRMATION = "Lidmaatschap stopgezet";

  private static final String SUBJECT_REACTIVATION_CONFIRMATION = "Welkom terug!";

  private static final String SUBJECT_PENDING_PAYMENT_CONFIRMATION = "Wil je nog iets wijzigen?";

  private static final String SUBJECT_REFERRAL_CODE_PAID = "Je Viteezy referral code is gebruikt!";

  private static final String SUBJECT_MISSING_PAYMENT = "Openstaande betaling Viteezy";
  private static final String MISSING_PAYMENT_LINK_URL = "%s/payment-request/%s";

  private static final String CONTEXT_NAME = "name";
  private static final String CONTEXT_DATE = "date";
  private static final String CONTEXT_AMOUNT = "amount";
  private static final String CONTEXT_DISCOUNT = "discount";
  private static final String CONTEXT_REFERRAL_CODE_USED_BY_NAME = "referralCodeUsedByName";

  private static final String LANGUAGE = "nl";
  private static final String AUTHENTICATED_REDIRECT_URL = "%s/domain?reference=%s";

  protected EmailSenderImpl(SpringTemplateEngine springTemplateEngine, JavaMailSender javaMailSender,
                            SlackService slackService, MailConfiguration mailConfiguration) {
    this.springTemplateEngine = springTemplateEngine;
    this.javaMailSender = javaMailSender;
    this.slackService = slackService;
    this.frontendBaseUrl = mailConfiguration.getFrontendBaseUrl();
    this.defaultOrderBcc = mailConfiguration.getDefaultOrderBcc();
    this.pharmacistCsvRecipient = mailConfiguration.getPharmacistCsvRecipient();
    this.pharmacistCsvSubject = mailConfiguration.getPharmacistCsvSubject();
  }

  @Override
  public Try<Void> sendMagicLink(String email, String token) {
    return Try.run(() -> {
      Context context = new Context();
      final String href = String.format(MAGIC_LINK_URL, frontendBaseUrl, token, URLEncoder.encode(email, StandardCharsets.UTF_8));
      context.setVariable("href", href);
      final String html = springTemplateEngine.process("magic-link-template", context);

      MimeMessagePreparator preparator = message -> {
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name());
        helper.setTo(email);
        helper.setSubject(SUBJECT_MAGIC_LINK);
        helper.setFrom(SENDER);
        helper.setText(html, true);

      };
      javaMailSender.send(preparator);
    });
  }

  @Override
  public Try<Void> sendOrderConfirmation(CustomerOrder customerOrder) {
    final Customer customer = customerOrder.getCustomer();
    final String email = customer.getEmail();
    final String firstName = customer.getFirstName();
    final String lastName = customer.getLastName();
    final String street = customer.getStreet();
    final Integer houseNumber = customer.getHouseNumber();
    final String houseNumberAddition = customer.getHouseNumberAddition() == null ? "" : customer.getHouseNumberAddition();
    final String city = customer.getCity();
    final String postcode = customer.getPostcode();
    final String country = customer.getCountry();
    final Locale locale = new Locale(LANGUAGE, country);

    final Long customerId = customer.getId();
    final BigDecimal recurringAndNotDiscountedAmount = customerOrder.getSubTotal().setScale(ROUNDING_MODE_DIGITS, RoundingMode.HALF_EVEN);
    final BigDecimal discountTotal = customerOrder.getDiscountTotal().setScale(ROUNDING_MODE_DIGITS, RoundingMode.HALF_EVEN);
    final BigDecimal shippingTotal = customerOrder.getShippingTotal().setScale(ROUNDING_MODE_DIGITS, RoundingMode.HALF_EVEN);
    final BigDecimal taxTotal = customerOrder.getTaxTotal().setScale(ROUNDING_MODE_DIGITS, RoundingMode.HALF_EVEN);
    final BigDecimal orderTotal = customerOrder.getOrderTotal().setScale(ROUNDING_MODE_DIGITS, RoundingMode.HALF_EVEN);

    String ingredientNames = getIngredientNames(customerOrder.getIngredients());

    return Try.run(() -> {
      Context context = new Context();
      context.setVariable("firstName", firstName);
      context.setVariable("lastName", lastName);
      context.setVariable("street", street);
      context.setVariable("houseNumber", houseNumber);
      context.setVariable("houseNumberAddition", houseNumberAddition);
      context.setVariable("city", city);
      context.setVariable("postcode", postcode);
      context.setVariable("country", locale.getDisplayCountry(locale));
      context.setVariable("customerId", customerId);
      context.setVariable("ingredientNames", ingredientNames);
      context.setVariable("recurringAndNotDiscountedAmount", recurringAndNotDiscountedAmount);
      context.setVariable("discountTotal", discountTotal);
      context.setVariable("shippingTotal", shippingTotal);
      context.setVariable("taxTotal", taxTotal);
      context.setVariable("orderTotal", orderTotal);
      context.setVariable("isSubscription", PaymentPlanStatus.ACTIVE.equals(customerOrder.getPaymentPlanStatus()));
      final String html = springTemplateEngine.process("order-confirmation", context);
      prepareAndSendMail(SUBJECT_ORDER_CONFIRMATION, email, html);
    });
  }

  @Override
  public Try<Void> sendChangeDeliveryDateConfirmation(String email, String firstName, LocalDateTime deliveryDate) {
    return Try.run(() -> {
      final Locale locale = new Locale(LANGUAGE);
      final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", locale);
      final String localeDeliveryDate = deliveryDate.getDayOfMonth() + " " + deliveryDate.format(monthFormatter) + " " + deliveryDate.getYear();
      Context context = new Context();
      context.setVariable(CONTEXT_NAME, firstName);
      context.setVariable(CONTEXT_DATE, localeDeliveryDate);

      final String html = springTemplateEngine.process("change-delivery-date-confirmation", context);
      prepareAndSendMail(SUBJECT_CHANGE_DELIVERY_DATE, email, html);
    });
  }

  @Override
  public Try<Void> sendStopConfirmation(String email, String firstName) {
    return Try.run(() -> {
      Context context = new Context();
      context.setVariable(CONTEXT_NAME, firstName);

      final String html = springTemplateEngine.process("stop-confirmation", context);
      prepareAndSendMail(SUBJECT_STOP_CONFIRMATION, email, html);
    });
  }

  @Override
  public Try<Void> sendReactivationConfirmation(String email, String firstName, LocalDateTime deliveryDate) {
    return Try.run(() -> {
      final Locale locale = new Locale(LANGUAGE);
      final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", locale);
      final String localeDeliveryDate = deliveryDate.getDayOfMonth() + " " + deliveryDate.format(monthFormatter) + " " + deliveryDate.getYear();
      Context context = new Context();
      context.setVariable(CONTEXT_NAME, firstName);
      context.setVariable(CONTEXT_DATE, localeDeliveryDate);

      final String html = springTemplateEngine.process("reactivation-confirmation", context);
      prepareAndSendMail(SUBJECT_REACTIVATION_CONFIRMATION, email, html);
    });
  }

  @Override
  public Try<Void> sendPendingPaymentConfirmation(Customer customer, Seq<Ingredient> ingredients) {
    return Try.run(() -> {
      Context context = new Context();
      String ingredientNames = getIngredientNames(ingredients);
      context.setVariable("ingredientNames", ingredientNames);
      context.setVariable(CONTEXT_NAME, customer.getFirstName());
      context.setVariable("domainLink", createAuthenticatedDomain(customer.getExternalReference()));

      final String html = springTemplateEngine.process("pending-payment-confirmation", context);
      prepareAndSendMail(SUBJECT_PENDING_PAYMENT_CONFIRMATION, customer.getEmail(), html);
    });
  }

  private URI createAuthenticatedDomain(UUID externalReference) {
    return URI.create(String.format(AUTHENTICATED_REDIRECT_URL, frontendBaseUrl, externalReference));
  }

  private String getIngredientNames(Seq<Ingredient> ingredients) {
    return ingredients
            .map(Ingredient::getName)
            .intersperse(", ")
            .reduce(String::concat)
            .trim();
  }

  @Override
  public Try<Void> sendReferralCodePaid(String email, String firstName, String referralCodeUsedByFirstName, ReferralDiscount referralDiscount) {
    return Try.run(() -> {
      Context context = new Context();
      context.setVariable(CONTEXT_NAME, firstName);

      context.setVariable(CONTEXT_REFERRAL_CODE_USED_BY_NAME, referralCodeUsedByFirstName);
      context.setVariable(CONTEXT_DISCOUNT, referralDiscount.getDiscountAmount());

      final String html = springTemplateEngine.process("referral-code-paid", context);
      prepareAndSendMail(SUBJECT_REFERRAL_CODE_PAID, email, html);
    });
  }

  @Override
  public Try<Void> sendPharmacistRequest(List<File> pharmacistRequestFiles) {
    return Try.run(() -> {
      MimeMessagePreparator preparator = message -> {
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name());
        helper.setTo(pharmacistCsvRecipient);
        if (StringUtils.isNotEmpty(defaultOrderBcc)) {
          helper.setBcc(defaultOrderBcc);
        }
        helper.setSubject(pharmacistCsvSubject);
        helper.setFrom(SENDER);
        if (pharmacistRequestFiles.isEmpty()) {
          helper.setText("De apotheker bestelling is leeg vandaag. Order list is empty today.\n\nGroet backend Viteezy", false);
        } else {
          pharmacistRequestFiles.forEach(file -> Try.run(() -> helper.addAttachment(file.getName(),file)));
        }
      };
      javaMailSender.send(preparator);
    }).onFailure(slackService::notifyPharmacistRequest);
  }

  @Override
  public Try<Void> sendPaymentMissing(String firstName, String email, Payment payment, UUID paymentPlanExternalReference, int attempts) {
    return Try.run(() -> {
      final String templateName = getTemplateNamePaymentMissing(payment, attempts);
      final String href = String.format(MISSING_PAYMENT_LINK_URL, frontendBaseUrl, paymentPlanExternalReference);
      Context context = new Context();
      context.setVariable("href", href);
      context.setVariable(CONTEXT_NAME, firstName);
      context.setVariable(CONTEXT_AMOUNT, payment.getAmount());
      final String html = springTemplateEngine.process(templateName, context);
      final String subject = getSubjectPaymentMissing(payment, attempts);
      prepareAndSendMail(subject, email, html);
    });
  }

  private String getTemplateNamePaymentMissing(Payment payment, int attempts) {
    if (attempts > 1) {
      if (attempts == 3 && PaymentStatus.failed.equals(payment.getStatus())) {
        return "payment-request-failed-reminder-3";
      } else {
        return "payment-request-reminder-" + attempts;
      }
    } else if (PaymentReason.DELIBERATELY_REVERSED.equals(payment.getReason())) {
      return "payment-request-deliberately-reversed";
    } else if (PaymentStatus.chargeback.equals(payment.getStatus())) {
      return "payment-request-chargeback-other";
    } else {
      return "payment-request-failed";
    }
  }

  private String getSubjectPaymentMissing(Payment payment, int attempts) {
    if (attempts == 2) {
      return SUBJECT_SECOND_GENERIC_REASON;
    } else if (attempts == 3 && PaymentStatus.failed.equals(payment.getStatus())) {
      return SUBJECT_THIRD_FAILED_REASON;
    } else if (attempts == 3) {
      return SUBJECT_THIRD_CHARGEBACK_REASON;
    } else {
      return SUBJECT_MISSING_PAYMENT;
    }
  }

  private void prepareAndSendMail(String subject, String email, String html) {
    MimeMessagePreparator preparator = message -> {
      MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED,
              StandardCharsets.UTF_8.name());
      helper.setTo(email);
      helper.setSubject(subject);
      helper.setFrom(SENDER);
      helper.setText(html, true);
      helper.addInline(VITEEZY_LOGO_CONTENT_ID, new ClassPathResource(VITEEZY_LOGO));
    };
    javaMailSender.send(preparator);
  }
}
