package viteezy.service.payment;

import be.woutschoovaerts.mollie.Client;
import be.woutschoovaerts.mollie.ClientBuilder;
import be.woutschoovaerts.mollie.data.common.AddressRequest;
import be.woutschoovaerts.mollie.data.common.Amount;
import be.woutschoovaerts.mollie.data.common.Locale;
import be.woutschoovaerts.mollie.data.customer.CustomerRequest;
import be.woutschoovaerts.mollie.data.customer.CustomerResponse;
import be.woutschoovaerts.mollie.data.method.MethodResponse;
import be.woutschoovaerts.mollie.data.payment.PaymentMethod;
import be.woutschoovaerts.mollie.data.payment.PaymentRequest;
import be.woutschoovaerts.mollie.data.payment.PaymentResponse;
import be.woutschoovaerts.mollie.data.payment.SequenceType;
import be.woutschoovaerts.mollie.exception.MollieException;
import be.woutschoovaerts.mollie.util.QueryParams;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.configuration.PaymentConfiguration;
import viteezy.domain.Customer;
import viteezy.domain.payment.MollieMetadataKeys;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentPlan;

import java.math.BigDecimal;
import java.net.URI;
import java.util.*;
import java.util.function.Consumer;

public class MollieServiceImpl implements MollieService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MollieService.class);
    protected static final String EUR = "EUR";

    private final Client client;
    private final URI redirectUrl;
    private final URI requestRedirectUrl;
    private final URI webhookUrl;

    protected MollieServiceImpl(PaymentConfiguration paymentConfiguration) {
        this.client = buildClient(paymentConfiguration.getApiKey());
        this.redirectUrl = paymentConfiguration.getRedirectUrl();
        this.requestRedirectUrl = paymentConfiguration.getRequestRedirectUrl();
        this.webhookUrl = paymentConfiguration.getWebhookUrl();
    }

    @Override
    public Try<CustomerResponse> createCustomer(String email) {
        CustomerRequest customerRequest = buildCustomerRequestForEmail(email);
        return Try.of(() -> client.customers().createCustomer(customerRequest))
                .onFailure(peekException(customerRequest));
    }

    public Try<CustomerResponse> updateCustomer(Customer customer) {
        CustomerRequest customerRequest = buildCustomerRequest(customer);
        return Try.of(() -> client.customers().updateCustomer(customer.getMollieCustomerId(), customerRequest))
                .onFailure(peekException(customerRequest));
    }

    @Override
    public Try<PaymentResponse> createCustomerPayment(Customer customer, BigDecimal amount, String description,
                                                      UUID externalReference, Optional<Long> referralId,
                                                      String method, String facebookClick, String gaCookie) {
        PaymentRequest paymentRequest = buildPaymentRequest(amount, description, externalReference, customer, referralId, method, facebookClick, gaCookie);
        return Try.of(() -> client.customers().createCustomerPayment(customer.getMollieCustomerId(), paymentRequest)).onFailure(peekException(paymentRequest));
    }

    @Override
    public Try<PaymentResponse> createRecurringPayment(Customer customer, BigDecimal amount, String description, UUID externalReference) {
        PaymentRequest paymentRequest = buildPaymentRequestRecurring(amount, description, externalReference, customer);
        return Try.of(() -> client.payments().createPayment(paymentRequest))
                .onFailure(peekException(paymentRequest));
    }

    private Consumer<Throwable> peekException(Object request) {
        return throwable -> {
            if (throwable instanceof MollieException) {
                LOGGER.error("request={}, error={}, details={}", request, throwable.getMessage(), ((MollieException) throwable).getDetails());
            } else {
                LOGGER.error("request={}, error={}", request, throwable.getMessage());
            }
        };
    }

    @Override
    public Try<PaymentResponse> find(String paymentId) {
        return Try.of(() -> client.payments().getPayment(paymentId));
    }

    @Override
    public Try<PaymentResponse> createRetryFirstPayment(Customer customer, PaymentPlan paymentPlan, Payment payment) {
        final String description = "Viteezy retry " + payment.getMolliePaymentId();
        final PaymentRequest paymentRequest = buildRetryFirstPayment(payment, description, paymentPlan.externalReference(), customer);
        return Try.of(() -> client.customers().createCustomerPayment(customer.getMollieCustomerId(), paymentRequest)).onFailure(peekException(paymentRequest));
    }

    @Override
    public Try<List<MethodResponse>> getPaymentMethods(String country) {
        final QueryParams params = new QueryParams();
        params.put("includeWallets", "applepay");
        params.put("sequenceType", "first");
        params.put("locale", "BE".equals(country) ? Locale.nl_BE.toString() : Locale.nl_NL.toString());
        params.put("billingCountry", country);
        return Try.of(() -> client.methods().listMethods(params).getEmbedded().getMethods());
    }


    private CustomerRequest buildCustomerRequestForEmail(String email) {
        return CustomerRequest.builder()
                .email(Optional.of(email))
                .build();
    }

    private CustomerRequest buildCustomerRequest(Customer customer) {
        return CustomerRequest.builder()
                .name(Optional.of(customer.getFirstName() + " " + customer.getLastName()))
                .email(Optional.of(customer.getEmail()))
                .locale(Optional.of("BE".equals(customer.getCountry()) ? Locale.nl_BE : Locale.nl_NL))
                .build();
    }

    private PaymentRequest buildPaymentRequest(BigDecimal amount, String description, UUID externalReference,
                                               Customer customer, Optional<Long> referralId, String method,
                                               String facebookClick, String gaCookie) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(MollieMetadataKeys.PAYMENT_PLAN_ID, externalReference);
        metadata.put(MollieMetadataKeys.FACEBOOK_CLICK_ID, facebookClick);
        metadata.put(MollieMetadataKeys.GA_SESSION_ID, gaCookie);
        referralId.ifPresent(aLong -> metadata.put(MollieMetadataKeys.REFERRAL_ID, aLong));
        final Optional<List<PaymentMethod>> paymentMethod = getPaymentMethod(method);
        final AddressRequest addressRequest = buildAddressRequest(customer);
        return PaymentRequest.builder()
                .amount(new Amount(EUR, amount))
                .description(description)
                .shippingAddress(Optional.of(addressRequest))
                .billingAddress(Optional.of(addressRequest))
                .metadata(metadata)
                .sequenceType(Optional.of(SequenceType.FIRST))
                .redirectUrl(redirectUrl.toString()+"/"+externalReference)
                .webhookUrl(Optional.of(webhookUrl.toString()))
                .locale(Optional.of("BE".equals(customer.getCountry()) ? Locale.nl_BE : Locale.nl_NL))
                .method(paymentMethod)
                .build();
    }

    private PaymentRequest buildPaymentRequestRecurring(BigDecimal amount, String description, UUID externalReference, Customer customer) {
        return PaymentRequest.builder()
                .customerId(Optional.of(customer.getMollieCustomerId()))
                .amount(new Amount(EUR, amount))
                .description(description)
                .metadata(Map.of(MollieMetadataKeys.PAYMENT_PLAN_ID, externalReference))
                .sequenceType(Optional.of(SequenceType.RECURRING))
                .webhookUrl(Optional.of(webhookUrl.toString()))
                .build();
    }

    private PaymentRequest buildRetryFirstPayment(Payment payment, String description, UUID externalReference, Customer customer) {
        final AddressRequest addressRequest = buildAddressRequest(customer);
        return PaymentRequest.builder()
                .amount(new Amount(EUR, payment.getAmount()))
                .description(description)
                .shippingAddress(Optional.of(addressRequest))
                .billingAddress(Optional.of(addressRequest))
                .metadata(Map.of(MollieMetadataKeys.PAYMENT_PLAN_ID, externalReference, MollieMetadataKeys.PAYMENT_ID, payment.getMolliePaymentId()))
                .sequenceType(Optional.of(SequenceType.FIRST))
                .redirectUrl(requestRedirectUrl.toString()+"/"+externalReference)
                .webhookUrl(Optional.of(webhookUrl.toString()))
                .locale(Optional.of("BE".equals(customer.getCountry()) ? Locale.nl_BE : Locale.nl_NL))
                .build();
    }

    private AddressRequest buildAddressRequest(Customer customer) {
        return AddressRequest.builder()
                .streetAndNumber(customer.getStreet() + " " + customer.getHouseNumber() + (StringUtils.isBlank(customer.getHouseNumberAddition()) ? "" : " " + customer.getHouseNumberAddition()))
                .postalCode(Optional.ofNullable(customer.getPostcode()))
                .city(customer.getCity())
                .country(customer.getCountry())
                .build();
    }

    private Client buildClient(String apiKey) {
        return new ClientBuilder()
                    .withApiKey(apiKey)
                    .build();
    }

    private Optional<List<PaymentMethod>> getPaymentMethod(String method) {
        for (PaymentMethod paymentMethod: PaymentMethod.values()) {
            if (paymentMethod.getJsonValue().equals(method)) {
                return Optional.of(List.of(paymentMethod));
            }
        }
        return Optional.empty();
    }
}
