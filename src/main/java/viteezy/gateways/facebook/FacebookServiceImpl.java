package viteezy.gateways.facebook;

import com.facebook.ads.sdk.serverside.*;
import com.facebook.ads.utils.ServerSideApiConstants;
import com.google.common.base.Throwables;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.db.quiz.GenderAnswerRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.Customer;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.quiz.GenderAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.gateways.slack.SlackService;
import viteezy.traits.EnforcePresenceTrait;

import java.util.UUID;
import java.util.function.Consumer;

public class FacebookServiceImpl implements FacebookService, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacebookService.class);
    private static final String CURRENCY = "eur";
    private final static String PRODUCT_ID = "viteezy-blend-v2";
    private final static String PURCHASE_EVENT_NAME = "Purchase";
    private final static String PURCHASE_RECURRING_EVENT_NAME = "RecurringSubscriptionPayment";
    private final static String QUIZ_START_EVENT_NAME = "Quiz Start";
    private static final String LEAD_EVENT_NAME = "Lead";

    private final String frontendBaseUrl;
    private final String facebookTestEventCode;

    private final GenderAnswerRepository genderAnswerRepository;
    private final QuizRepository quizRepository;
    private final FacebookClient facebookClient;
    private final SlackService slackService;

    protected FacebookServiceImpl(String frontendBaseUrl, String facebookTestEventCode,
                                  GenderAnswerRepository genderAnswerRepository, QuizRepository quizRepository,
                                  FacebookClient facebookClient, SlackService slackService) {
        this.frontendBaseUrl = frontendBaseUrl;
        this.facebookTestEventCode = facebookTestEventCode;
        this.genderAnswerRepository = genderAnswerRepository;
        this.quizRepository = quizRepository;
        this.facebookClient = facebookClient;
        this.slackService = slackService;
    }

    @Override
    public void sendPurchaseEvent(Customer customer, PaymentPlan paymentPlan, boolean first, String molliePaymentId, String facebookClick) {
        final UserData userData = buildUserData(customer, paymentPlan, facebookClick)
                        .gender(findGender(customer.getId()));
        // only send the externalId when first payment, because for recurring there's no pixel sent to FB
        final UserData userExternalIdData = first ? userData.externalId(customer.getExternalReference().toString()) : userData;
        final Event event = buildPurchaseEvent(paymentPlan, userExternalIdData, first, molliePaymentId);
        sendEventRequest(event);
    }

    @Override
    public void sendStartQuiz(Quiz quiz, String facebookPixel, String facebookClick, String userAgent, String userIpAddress, String referer) {
        final UserData userData = buildUserData(facebookPixel, facebookClick, userAgent, userIpAddress);
        final Event event = buildStartQuizEvent(quiz, userData, referer);
        sendEventRequest(event);
    }

    @Override
    public void sendLeadEvent(Customer customer, UUID quizExternalReference, String facebookClick) {
        final UserData userData = buildUserData(customer, facebookClick)
                        .externalId(customer.getExternalReference().toString())
                        .gender(findGender(quizExternalReference));
        final Event event = buildLeadEvent(quizExternalReference, userData);
        sendEventRequest(event);
    }

    private GenderEnum findGender(Long customerId) {
        return quizRepository.findByCustomerId(customerId)
                .flatMap(optionalToNarrowedTry())
                .map(quiz -> findGender(quiz.getExternalReference()))
                .getOrElse((GenderEnum) null);
    }

    private GenderEnum findGender(UUID quizExternalReference) {
        return genderAnswerRepository.find(quizExternalReference)
                .map(nameAnswer -> nameAnswer.map(GenderAnswer::getGenderId)
                        .map(this::buildGender)
                        .orElse(null))
                .getOrElse((GenderEnum) null);
    }

    private GenderEnum buildGender(Long gender) {
        switch (gender.intValue()) {
            case 1: return GenderEnum.FEMALE;
            case 2: return GenderEnum.MALE;
            default: return null;
        }
    }

    private void sendEventRequest(Event event) {
        facebookClient.sendEventRequest(event)
                .onFailure(this::slackException)
                .onFailure(peekException(event));
    }

    private Event buildPurchaseEvent(PaymentPlan paymentPlan, UserData userData, boolean first, String molliePaymentId) {
        final CustomData customData = buildCustomData(paymentPlan, first);
        return new Event()
                .eventName(first ? PURCHASE_EVENT_NAME : PURCHASE_RECURRING_EVENT_NAME)
                .eventTime(System.currentTimeMillis() / 1000L)
                .eventId(first ? paymentPlan.externalReference().toString() : molliePaymentId)
                .userData(userData)
                .customData(customData)
                .eventSourceUrl(frontendBaseUrl + "/confirmed/" + paymentPlan.externalReference())
                .actionSource(ActionSource.website);
    }

    private Event buildStartQuizEvent(Quiz quiz, UserData userData, String referer) {
        return new Event()
                .eventName(QUIZ_START_EVENT_NAME)
                .eventTime(System.currentTimeMillis() / 1000L)
                .eventId(quiz.getExternalReference().toString())
                .userData(userData)
                .eventSourceUrl(StringUtils.isNotBlank(referer) ? referer : frontendBaseUrl + "/quiz-v2")
                .actionSource(ActionSource.website);
    }

    private Event buildLeadEvent(UUID quizExternalReference, UserData userData) {
        return new Event()
                .eventName(LEAD_EVENT_NAME)
                .eventTime(System.currentTimeMillis() / 1000L)
                .eventId(quizExternalReference.toString())
                .userData(userData)
                .eventSourceUrl(frontendBaseUrl + "/quiz-v2/email")
                .actionSource(ActionSource.website);
    }

    private CustomData buildCustomData(PaymentPlan paymentPlan, boolean first) {
        final Float itemPrice = first ? paymentPlan.firstAmount().floatValue(): paymentPlan.recurringAmount().floatValue();
        final Content content = buildContent(itemPrice);
        return new CustomData()
                .addContent(content)
                .currency(CURRENCY)
                .orderId(paymentPlan.externalReference().toString())
                .value(itemPrice);
    }

    private Content buildContent(Float itemPrice) {
        return new Content()
                .productId(PRODUCT_ID)
                .quantity(1L)
                .itemPrice(itemPrice)
                .deliveryCategory(DeliveryCategory.home_delivery);
    }

    private UserData buildUserData(String facebookPixel, String facebookClick, String userAgent, String userIpAddress) {
        return new UserData()
                .clientUserAgent(userAgent)
                .clientIpAddress(userIpAddress)
                .fbp(facebookPixel)
                .fbc(facebookClick);
    }

    private UserData buildUserData(Customer customer, String facebookClick) {
        return new UserData()
                .clientUserAgent(customer.getUserAgent())
                .clientIpAddress(customer.getUserIpAddress())
                .email(normalizeEmail(customer.getEmail()))
                .phone(normalizePhoneNumber(customer))
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .zipcode(StringUtils.lowerCase(customer.getPostcode()))
                .city(normalizeCity(customer.getCity()))
                .countryCode(StringUtils.lowerCase(customer.getCountry()))
                .fbp(customer.getFacebookPixel())
                .fbc(facebookClick);
    }

    private UserData buildUserData(Customer customer, PaymentPlan paymentPlan, String facebookClick) {
        return buildUserData(customer, facebookClick)
                .subscriptionId(paymentPlan.externalReference().toString());
    }

    private String normalizeCity(String city) {
        return Try.of(() -> com.facebook.ads.utils.ServerSideApiUtil.normalize(StringUtils.lowerCase(city), ServerSideApiConstants.CITY)).getOrNull();
    }

    private String normalizeEmail(String email) {
        return Try.of(() -> com.facebook.ads.utils.ServerSideApiUtil.normalize(email, ServerSideApiConstants.EMAIL)).getOrNull();
    }

    private String normalizePhoneNumber(Customer customer) {
        if (customer.getPhoneNumber() == null) return null;
        final String finalPhoneNumber;
        String phoneNumber = customer.getPhoneNumber().replaceAll("[^0-9]", "");
        if (phoneNumber.startsWith("00")) {
            finalPhoneNumber = phoneNumber.substring(2);
        } else
        if (phoneNumber.startsWith("0")) {
            phoneNumber = phoneNumber.substring(1);
            switch (StringUtils.defaultIfEmpty(customer.getCountry(), "")) {
                case "NL":
                    finalPhoneNumber = "31" + phoneNumber;
                    break;
                case "BE":
                    finalPhoneNumber = "32" + phoneNumber;
                    break;
                default:
                    finalPhoneNumber = phoneNumber;
            }
        } else
            finalPhoneNumber = phoneNumber;
        return Try.of(() -> com.facebook.ads.utils.ServerSideApiUtil.normalize(finalPhoneNumber, ServerSideApiConstants.PHONE_NUMBER)).getOrNull();
    }

    private void slackException(Throwable throwable) {
        if (StringUtils.isBlank(facebookTestEventCode)) {
            slackService.notify(throwable);
        }
    }

    private Consumer<Throwable> peekException(Event event) {
        return throwable -> LOGGER.error("event={}, throwable={}", event, Throwables.getStackTraceAsString(throwable));
    }
}
