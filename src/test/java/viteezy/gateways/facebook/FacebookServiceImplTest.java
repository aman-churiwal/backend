package viteezy.gateways.facebook;

import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import viteezy.db.quiz.GenderAnswerRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.Customer;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.ConfigurationDatabaseObject;
import viteezy.domain.quiz.GenderAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.payment.PaymentPlanStatus;
import viteezy.gateways.slack.SlackService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class FacebookServiceImplTest {

    private static final String FRONTEND_BASE_URL = "http://localhost:3000";
    private static final String FACEBOOK_CLICK = "fb.1.1234567890123.AbCdEfGhIjKlMnOpQrStUvWxYz1234567890";

    private static final Long CUSTOMER_ID = 1L;
    private static final Long PAYMENT_PLAN_ID = 2L;
    private static final String GA_ID = "gaId";
    private static final String USER_AGENT = "userAgent";
    private static final Long BLEND_ID = 3L;
    private static final BigDecimal FIRST_AMOUNT = new BigDecimal("10");
    private static final BigDecimal RECURRING_AMOUNT = new BigDecimal("10");
    private static final Integer RECURRING_MONTHS = 1;
    private static final UUID EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final PaymentPlanStatus SUBSCRIPTION_STATUS_ACTIVE = PaymentPlanStatus.ACTIVE;
    private static final Long QUIZ_ID = 4L;
    private static final GenderAnswer GENDER_ANSWER = new GenderAnswer(null, QUIZ_ID, 1L, null, null);
    private static final UUID QUIZ_EXTERNAL_REFERENCE = UUID.randomUUID();

    private static final Customer CUSTOMER = new Customer(CUSTOMER_ID, "email", true, UUID.randomUUID(),
            "mollieCustomerId", null, null, null, GA_ID, "fb.1.1234567890123.12345678", USER_AGENT, null, null, null, null, null, null, null, null,null, "NL",
            null, LocalDateTime.now(), LocalDateTime.now());
    private static final PaymentPlan PAYMENT_PLAN = new PaymentPlan(PAYMENT_PLAN_ID, FIRST_AMOUNT, RECURRING_AMOUNT, RECURRING_MONTHS,
            CUSTOMER_ID,BLEND_ID, EXTERNAL_REFERENCE, LocalDateTime.now(), LocalDateTime.now(),
            SUBSCRIPTION_STATUS_ACTIVE, LocalDateTime.now(), null, LocalDateTime.now(), Optional.empty(),
            Optional.empty(), null);
    private static final ConfigurationDatabaseObject CONFIGURATION_DATABASE_OBJECT = new ConfigurationDatabaseObject(null, "facebook_access_token", "bearer", "EAAFjlRfrLCMBAB5y0wHtKPVrjHLysnoNmChzhwHf6DZCuNeSGUQe99wy9tzxuZBmCtcLKyCZBnJGr32RSfma1hwbX9QXEZCIdyoDc1O3R0vN6vPz0IWXeCvJGQjcZAMmWwdShnHj9gmGATzpgZCWwUJ6XEqMyF9apiM3xb7pPUanCgrZCbeeU5d", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
    private static final Quiz QUIZ = new Quiz(QUIZ_ID, QUIZ_EXTERNAL_REFERENCE, LocalDateTime.now(), LocalDateTime.now(), CUSTOMER_ID, null);
    private static final IllegalStateException EXCEPTION = new IllegalStateException();

    private final GenderAnswerRepository genderAnswerRepository = Mockito.mock(GenderAnswerRepository.class);
    private final QuizRepository quizRepository = Mockito.mock(QuizRepository.class);
    private final FacebookClient facebookClient = Mockito.mock(FacebookClient.class);
    private final SlackService slackService = Mockito.mock(SlackService.class);
    private FacebookService facebookService;

    @BeforeEach
    void setup() {
        facebookService = new FacebookServiceImpl(FRONTEND_BASE_URL, "", genderAnswerRepository, quizRepository, facebookClient, slackService);
    }


    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(genderAnswerRepository, quizRepository, facebookClient, slackService);
        reset(genderAnswerRepository, quizRepository, facebookClient, slackService);
    }

    @Test
    void sendPurchaseEventFirst() {
        Mockito.when(facebookClient.sendEventRequest(Mockito.any())).thenReturn(Try.success(null));
        Mockito.when(quizRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(Try.success(Optional.of(QUIZ)));
        Mockito.when(genderAnswerRepository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(Optional.of(GENDER_ANSWER)));

        facebookService.sendPurchaseEvent(CUSTOMER, PAYMENT_PLAN, true, PAYMENT_PLAN.externalReference().toString(), FACEBOOK_CLICK);

        Mockito.verify(facebookClient).sendEventRequest(Mockito.any());
        Mockito.verify(genderAnswerRepository).find(QUIZ_EXTERNAL_REFERENCE);
        Mockito.verify(quizRepository).findByCustomerId(CUSTOMER_ID);
    }

    @Test
    void sendPurchaseEventRecurring() {
        Mockito.when(facebookClient.sendEventRequest(Mockito.any())).thenReturn(Try.success(null));
        Mockito.when(quizRepository.findByCustomerId(CUSTOMER_ID))
                .thenReturn(Try.success(Optional.of(QUIZ)));
        Mockito.when(genderAnswerRepository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(Optional.of(GENDER_ANSWER)));

        facebookService.sendPurchaseEvent(CUSTOMER, PAYMENT_PLAN, false, PAYMENT_PLAN.externalReference().toString(), FACEBOOK_CLICK);

        Mockito.verify(facebookClient).sendEventRequest(Mockito.any());
        Mockito.verify(genderAnswerRepository).find(QUIZ_EXTERNAL_REFERENCE);
        Mockito.verify(quizRepository).findByCustomerId(CUSTOMER_ID);
    }

    @Test
    void sendStartQuiz() {
        Mockito.when(facebookClient.sendEventRequest(Mockito.any())).thenReturn(Try.success(null));

        facebookService.sendStartQuiz(QUIZ, CUSTOMER.getFacebookPixel(), FACEBOOK_CLICK, USER_AGENT, "127.0.0.1", null);

        Mockito.verify(facebookClient).sendEventRequest(Mockito.any());
    }

    @Test
    void sendStartQuizFailed() {
        Mockito.when(facebookClient.sendEventRequest(Mockito.any())).thenReturn(Try.failure(EXCEPTION));

        facebookService.sendStartQuiz(QUIZ, CUSTOMER.getFacebookPixel(), FACEBOOK_CLICK, USER_AGENT, "127.0.0.1", null);

        Mockito.verify(facebookClient).sendEventRequest(Mockito.any());
        Mockito.verify(slackService).notify(EXCEPTION);
    }

    @Test
    void sendLeadEvent() {
        Mockito.when(facebookClient.sendEventRequest(Mockito.any())).thenReturn(Try.success(null));
        Mockito.when(genderAnswerRepository.find(QUIZ_EXTERNAL_REFERENCE))
                        .thenReturn(Try.success(Optional.of(GENDER_ANSWER)));

        facebookService.sendLeadEvent(CUSTOMER, QUIZ_EXTERNAL_REFERENCE, FACEBOOK_CLICK);

        Mockito.verify(facebookClient).sendEventRequest(Mockito.any());
        Mockito.verify(genderAnswerRepository).find(QUIZ_EXTERNAL_REFERENCE);
    }
}
