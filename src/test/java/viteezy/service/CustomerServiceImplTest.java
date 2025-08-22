package viteezy.service;

import be.woutschoovaerts.mollie.data.customer.CustomerResponse;
import io.vavr.control.Try;
import org.jdbi.v3.core.CloseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.NoTransactionException;
import viteezy.controller.dto.CustomerAddressPostRequest;
import viteezy.controller.dto.quiz.EmailPostRequest;
import viteezy.db.CustomerRepository;
import viteezy.db.quiz.NameAnswerRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.blend.Blend;
import viteezy.domain.Customer;
import viteezy.domain.klaviyo.KlaviyoConstant;
import viteezy.domain.klaviyo.profile.Profile;
import viteezy.domain.klaviyo.profile.ProfileAttributes;
import viteezy.domain.klaviyo.profile.ProfileData;
import viteezy.domain.quiz.NameAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.domain.blend.BlendStatus;
import viteezy.gateways.facebook.FacebookService;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.service.blend.BlendService;
import viteezy.service.payment.MollieService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    private static final Long CUSTOMER_ID = 2L;
    private static final Long QUIZ_ID = 3L;
    private static final Long BLEND_ID = 4L;
    private static final Long NAME_ANSWER_ID = 5L;
    private static final String CUSTOMER_EMAIL = "email";
    private static final UUID CUSTOMER_EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final UUID QUIZ_EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final UUID BLEND_EXTERNAL_REFERENCE = UUID.randomUUID();
    private static final String GA_ID = "1.1";
    private static final String FACEBOOK_PIXEL = "fb.1.1";
    private static final String FACEBOOK_CLICK = "fb.1.2";
    private static final String REFERRAL_CODE = "FIRSTNAME12345";
    private static final String USER_AGENT = "Mozilla";
    private static final String USER_IP_ADDRESS = "127.2.3.4";
    private static final Customer CUSTOMER = new Customer(CUSTOMER_ID, CUSTOMER_EMAIL, true, CUSTOMER_EXTERNAL_REFERENCE, null, null, null, "klaviyoid", GA_ID, FACEBOOK_PIXEL, USER_AGENT, USER_IP_ADDRESS, "name", "lastName", null, null, null, null, null, null, null, REFERRAL_CODE, LocalDateTime.now(), LocalDateTime.now());
    private static final CustomerResponse CUSTOMER_RESPONSE = mock(CustomerResponse.class);
    private static final CustomerAddressPostRequest CUSTOMER_ADDRESS_POST_REQUEST = new CustomerAddressPostRequest("name", "lastName", "06", CUSTOMER_EMAIL, "street", 1, "A", "1000AA", "plaats", "stad");
    private static final Quiz QUIZ = new Quiz(QUIZ_ID, QUIZ_EXTERNAL_REFERENCE, LocalDateTime.now(), LocalDateTime.now(), CUSTOMER_ID, null);
    private static final Blend BLEND = new Blend(BLEND_ID, BlendStatus.CREATED, BLEND_EXTERNAL_REFERENCE, CUSTOMER_ID, QUIZ_ID, LocalDateTime.now(), LocalDateTime.now());
    private static final CloseException EXCEPTION = new CloseException("EXCEPTION", new Throwable());
    private static final NameAnswer NAME_ANSWER = new NameAnswer(NAME_ANSWER_ID, QUIZ_ID, "name", LocalDateTime.now(), LocalDateTime.now());
    private static final EmailPostRequest EMAIL_POST_REQUEST = new EmailPostRequest(CUSTOMER_EMAIL, true, FACEBOOK_CLICK);
    private static final Profile PROFILE = new Profile(new ProfileData("123", "profile", new ProfileAttributes(CUSTOMER.getEmail(), CUSTOMER.getExternalReference().toString(), CUSTOMER.getFirstName(), null, null, null)));

    private final MollieService mollieService = mock(MollieService.class);
    private final CustomerRepository customerRepository = mock(CustomerRepository.class);

    private final QuizRepository quizRepository = mock(QuizRepository.class);
    private final BlendService blendService = mock(BlendService.class);
    private final KlaviyoService klaviyoService = mock(KlaviyoService.class);
    private final UtilityService utilityService = mock(UtilityService.class);
    private final NameAnswerRepository nameAnswerRepository = mock(NameAnswerRepository.class);
    private final FacebookService facebookService = mock(FacebookService.class);

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(
                mollieService,
                customerRepository,
                quizRepository,
                blendService,
                klaviyoService,
                utilityService,
                nameAnswerRepository,
                facebookService);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(
                mollieService,
                customerRepository,
                quizRepository,
                blendService,
                klaviyoService,
                utilityService,
                nameAnswerRepository,
                facebookService);

        reset(
                mollieService,
                customerRepository,
                quizRepository,
                blendService,
                klaviyoService,
                utilityService,
                nameAnswerRepository,
                facebookService);
    }

    @Test
    void findId() {
        when(customerRepository.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));

        Try<Customer> customerTry = customerService.find(CUSTOMER_ID);
        assertTrue(customerTry.isSuccess());
        assertEquals(customerTry.get(), CUSTOMER);

        verify(customerRepository).find(CUSTOMER_ID);
    }

    @Test
    void findEmail() {
        when(customerRepository.find(CUSTOMER_EMAIL))
                .thenReturn(Try.success(CUSTOMER));

        Try<Customer> optionalTry = customerService.find(CUSTOMER_EMAIL);
        assertTrue(optionalTry.isSuccess());
        assertEquals(optionalTry.get(), CUSTOMER);

        verify(customerRepository).find(CUSTOMER_EMAIL);
    }

    @Test
    void save() {
        when(customerRepository.save(CUSTOMER))
                .thenReturn(Try.success(CUSTOMER));

        Try<Customer> customerTry = customerService.save(CUSTOMER);
        assertTrue(customerTry.isSuccess());
        assertEquals(customerTry.get(), CUSTOMER);

        verify(customerRepository).save(CUSTOMER);
    }

    @Test
    void createCustomerOkSameCustomer() {
        when(customerRepository.findByBlend(BLEND.getExternalReference()))
                .thenReturn(Try.success(CUSTOMER));

        when(blendService.find(BLEND_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND));
        when(blendService.update(BLEND))
                .thenReturn(Try.success(BLEND));

        when(quizRepository.findOptional(QUIZ_ID))
                .thenReturn(Try.success(Optional.of(QUIZ)));
        when(quizRepository.update(buildQuiz()))
                .thenReturn(Try.success(QUIZ));
        when(customerRepository.update(any(Customer.class)))
                .thenReturn(Try.success(CUSTOMER));
        when(utilityService.buildReferralCode(CUSTOMER.getFirstName()))
                .thenReturn(REFERRAL_CODE);
        when(utilityService.getFirstName(CUSTOMER.getFirstName(), CUSTOMER.getLastName()))
                .thenReturn(CUSTOMER.getFirstName());
        when(customerRepository.updateReferralCode(CUSTOMER_ID, REFERRAL_CODE))
                .thenReturn(Try.success(CUSTOMER));
        when(customerRepository.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));
        when(mollieService.createCustomer(CUSTOMER.getEmail()))
                .thenReturn(Try.success(CUSTOMER_RESPONSE));
        when(klaviyoService.upsertInitialProfile(CUSTOMER, Optional.of(QUIZ)))
                .thenReturn(Try.success(null));

        Try<Customer> customerTry = customerService.createCustomer(BLEND_EXTERNAL_REFERENCE, CUSTOMER_ADDRESS_POST_REQUEST, GA_ID, FACEBOOK_PIXEL, USER_AGENT, USER_IP_ADDRESS);
        assertTrue(customerTry.isSuccess());
        assertEquals(customerTry.get(), CUSTOMER);

        verify(customerRepository).findByBlend(BLEND_EXTERNAL_REFERENCE);
        verify(blendService).find(BLEND_EXTERNAL_REFERENCE);
        verify(blendService).update(any(Blend.class));
        verify(customerRepository).update(any(Customer.class));
        verify(utilityService).buildReferralCode(CUSTOMER.getFirstName());
        verify(utilityService).getFirstName(CUSTOMER.getFirstName(), CUSTOMER.getLastName());
        verify(customerRepository).updateReferralCode(CUSTOMER_ID, REFERRAL_CODE);
        verify(quizRepository).findOptional(QUIZ_ID);
        verify(quizRepository).update(buildQuiz());
        verify(mollieService).createCustomer(CUSTOMER.getEmail());
        verify(klaviyoService).upsertInitialProfile(CUSTOMER, Optional.of(QUIZ));
    }

    @Test
    void createCustomerOkNotFoundExistingEmail() {
        when(customerRepository.findByBlend(BLEND.getExternalReference()))
                .thenReturn(Try.failure(EXCEPTION));
        when(customerRepository.findByEmailWithActivePaymentPlan(CUSTOMER_ADDRESS_POST_REQUEST.getEmail()))
                .thenReturn(Try.failure(new IllegalStateException()));
        when(customerRepository.find(CUSTOMER_ADDRESS_POST_REQUEST.getEmail()))
                .thenReturn(Try.failure(new IllegalStateException()));

        when(mollieService.createCustomer(CUSTOMER_ADDRESS_POST_REQUEST.getEmail()))
                .thenReturn(Try.success(CUSTOMER_RESPONSE));
        when(customerRepository.save(any(Customer.class)))
                .thenReturn(Try.success(CUSTOMER));

        when(blendService.find(BLEND_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND));
        when(blendService.update(BLEND))
                .thenReturn(Try.success(BLEND));

        when(quizRepository.findOptional(QUIZ_ID))
                .thenReturn(Try.success(Optional.of(QUIZ)));
        when(quizRepository.update(buildQuiz()))
                .thenReturn(Try.success(QUIZ));
        when(customerRepository.update(any(Customer.class)))
                .thenReturn(Try.success(CUSTOMER));
        when(utilityService.buildReferralCode(CUSTOMER.getFirstName()))
                .thenReturn(REFERRAL_CODE);
        when(utilityService.getFirstName(CUSTOMER.getFirstName(), CUSTOMER.getLastName()))
                .thenReturn(CUSTOMER.getFirstName());
        when(customerRepository.updateReferralCode(CUSTOMER_ID, REFERRAL_CODE))
                .thenReturn(Try.success(CUSTOMER));
        when(customerRepository.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));
        when(klaviyoService.upsertInitialProfile(CUSTOMER, Optional.of(QUIZ)))
                .thenReturn(Try.success(null));

        Try<Customer> customerTry = customerService.createCustomer(BLEND_EXTERNAL_REFERENCE, CUSTOMER_ADDRESS_POST_REQUEST, GA_ID, FACEBOOK_PIXEL, USER_AGENT, USER_IP_ADDRESS);
        assertTrue(customerTry.isSuccess());
        assertEquals(customerTry.get(), CUSTOMER);

        verify(customerRepository).findByBlend(BLEND_EXTERNAL_REFERENCE);
        verify(customerRepository).findByEmailWithActivePaymentPlan(CUSTOMER_ADDRESS_POST_REQUEST.getEmail());
        verify(customerRepository).find(CUSTOMER_ADDRESS_POST_REQUEST.getEmail());
        verify(mollieService).createCustomer(CUSTOMER_ADDRESS_POST_REQUEST.getEmail());
        verify(customerRepository).save(any(Customer.class));
        verify(blendService).find(BLEND_EXTERNAL_REFERENCE);
        verify(blendService).update(any(Blend.class));
        verify(customerRepository).update(any(Customer.class));
        verify(utilityService).buildReferralCode(CUSTOMER.getFirstName());
        verify(utilityService).getFirstName(CUSTOMER.getFirstName(), CUSTOMER.getLastName());
        verify(customerRepository).updateReferralCode(CUSTOMER_ID, REFERRAL_CODE);
        verify(quizRepository).findOptional(QUIZ_ID);
        verify(quizRepository).update(buildQuiz());
        verify(klaviyoService).upsertInitialProfile(CUSTOMER, Optional.of(QUIZ));
    }

    @Test
    void createCustomerOkFoundExistingEmail() {
        CustomerAddressPostRequest customerAddressPostRequest = new CustomerAddressPostRequest("name", "lastName", "06", "email2", "street", 1, "A", "1000AA", "plaats", "stad");

        when(customerRepository.findByBlend(BLEND.getExternalReference()))
                .thenReturn(Try.success(CUSTOMER));

        when(blendService.find(BLEND_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(BLEND));
        when(blendService.update(BLEND))
                .thenReturn(Try.success(BLEND));

        when(quizRepository.findOptional(QUIZ_ID))
                .thenReturn(Try.success(Optional.of(QUIZ)));
        when(quizRepository.update(buildQuiz()))
                .thenReturn(Try.success(QUIZ));
        when(customerRepository.update(any(Customer.class)))
                .thenReturn(Try.success(CUSTOMER));
        when(utilityService.buildReferralCode(CUSTOMER.getFirstName()))
                .thenReturn(REFERRAL_CODE);
        when(utilityService.getFirstName(CUSTOMER.getFirstName(), CUSTOMER.getLastName()))
                .thenReturn(CUSTOMER.getFirstName());
        when(customerRepository.updateReferralCode(CUSTOMER_ID, REFERRAL_CODE))
                .thenReturn(Try.success(CUSTOMER));
        when(mollieService.createCustomer(CUSTOMER.getEmail()))
                .thenReturn(Try.success(CUSTOMER_RESPONSE));
        when(customerRepository.find(CUSTOMER_ID))
                .thenReturn(Try.success(CUSTOMER));
        when(customerRepository.findByEmailWithActivePaymentPlan(customerAddressPostRequest.getEmail()))
                .thenReturn(Try.failure(new IllegalStateException()));
        when(customerRepository.find(customerAddressPostRequest.getEmail()))
                .thenReturn(Try.success(CUSTOMER));
        when(klaviyoService.upsertInitialProfile(CUSTOMER, Optional.of(QUIZ)))
                .thenReturn(Try.success(null));

        Try<Customer> customerTry = customerService.createCustomer(BLEND_EXTERNAL_REFERENCE, customerAddressPostRequest, GA_ID, FACEBOOK_PIXEL, USER_AGENT, USER_IP_ADDRESS);
        assertTrue(customerTry.isSuccess());
        assertEquals(customerTry.get(), CUSTOMER);

        verify(customerRepository).findByBlend(BLEND_EXTERNAL_REFERENCE);
        verify(blendService).find(BLEND_EXTERNAL_REFERENCE);
        verify(blendService).update(any(Blend.class));
        verify(customerRepository).update(any(Customer.class));
        verify(utilityService).buildReferralCode(CUSTOMER.getFirstName());
        verify(utilityService).getFirstName(CUSTOMER.getFirstName(), CUSTOMER.getLastName());
        verify(customerRepository).updateReferralCode(CUSTOMER_ID, REFERRAL_CODE);
        verify(quizRepository).findOptional(QUIZ_ID);
        verify(quizRepository).update(buildQuiz());
        verify(mollieService).createCustomer(CUSTOMER.getEmail());
        verify(customerRepository).findByEmailWithActivePaymentPlan(customerAddressPostRequest.getEmail());
        verify(customerRepository).find(customerAddressPostRequest.getEmail());
        verify(klaviyoService).upsertInitialProfile(CUSTOMER, Optional.of(QUIZ));
    }

    @Test
    void createCustomerFailFoundActiveExistingEmail() {
        CustomerAddressPostRequest customerAddressPostRequest = new CustomerAddressPostRequest("name", "lastName", "06", "email2", "street", 1, "A", "1000AA", "plaats", "stad");

        when(customerRepository.findByBlend(BLEND.getExternalReference()))
                .thenReturn(Try.success(CUSTOMER));
        when(customerRepository.findByEmailWithActivePaymentPlan(customerAddressPostRequest.getEmail()))
                .thenReturn(Try.success(CUSTOMER));

        Assertions.assertThrows(NoTransactionException.class, () -> customerService.createCustomer(BLEND_EXTERNAL_REFERENCE, customerAddressPostRequest, GA_ID, FACEBOOK_PIXEL, USER_AGENT, USER_IP_ADDRESS));

        verify(customerRepository).findByBlend(BLEND_EXTERNAL_REFERENCE);
        verify(customerRepository).findByEmailWithActivePaymentPlan(customerAddressPostRequest.getEmail());
    }

    @Test
    void saveEmailOk() {
        when(quizRepository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(QUIZ));
        when(customerRepository.findByEmailWithActivePaymentPlan(CUSTOMER_EMAIL))
                .thenReturn(Try.failure(new IllegalStateException()));
        when(customerRepository.find(CUSTOMER_EMAIL))
                .thenReturn(Try.failure(new IllegalStateException()));
        when(nameAnswerRepository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(Optional.of(NAME_ANSWER)));
        when(customerRepository.save(any(Customer.class)))
                .thenReturn(Try.success(CUSTOMER));
        when(klaviyoService.upsertInitialProfile(CUSTOMER, Optional.of(QUIZ)))
                .thenReturn(Try.success(CUSTOMER));
        when(quizRepository.update(buildQuiz()))
                .thenReturn(Try.success(QUIZ));
        doNothing().when(klaviyoService).createEvent(CUSTOMER, KlaviyoConstant.QUIZ_STARTED, null, Optional.empty());

        final Try<Customer> customerTry = customerService.saveEmailIfNotActive(QUIZ_EXTERNAL_REFERENCE, EMAIL_POST_REQUEST, GA_ID, FACEBOOK_PIXEL, FACEBOOK_CLICK, USER_AGENT, USER_IP_ADDRESS);
        assertTrue(customerTry.isSuccess());
        assertEquals(customerTry.get(), CUSTOMER);

        verify(quizRepository).find(QUIZ_EXTERNAL_REFERENCE);
        verify(customerRepository).findByEmailWithActivePaymentPlan(CUSTOMER_EMAIL);
        verify(customerRepository).find(CUSTOMER_EMAIL);
        verify(nameAnswerRepository).find(QUIZ_EXTERNAL_REFERENCE);
        verify(customerRepository).save(any(Customer.class));
        verify(klaviyoService).upsertInitialProfile(CUSTOMER, Optional.of(QUIZ));
        verify(quizRepository).update(buildQuiz());
        verify(facebookService).sendLeadEvent(CUSTOMER, QUIZ_EXTERNAL_REFERENCE, FACEBOOK_CLICK);
        verify(klaviyoService).createEvent(CUSTOMER, KlaviyoConstant.QUIZ_STARTED, null, Optional.empty());
    }

    @Test
    void saveEmailExists() {
        when(quizRepository.find(QUIZ_EXTERNAL_REFERENCE))
                .thenReturn(Try.success(QUIZ));
        when(customerRepository.findByEmailWithActivePaymentPlan(CUSTOMER_EMAIL))
                .thenReturn(Try.failure(new IllegalStateException()));
        when(customerRepository.find(CUSTOMER_EMAIL))
                .thenReturn(Try.success(CUSTOMER));

        final Try<Customer> customerTry = customerService.saveEmailIfNotActive(QUIZ_EXTERNAL_REFERENCE, EMAIL_POST_REQUEST, GA_ID, FACEBOOK_PIXEL, FACEBOOK_CLICK, USER_AGENT, USER_IP_ADDRESS);
        assertTrue(customerTry.isSuccess());

        verify(quizRepository).find(QUIZ_EXTERNAL_REFERENCE);
        verify(customerRepository).findByEmailWithActivePaymentPlan(CUSTOMER_EMAIL);
        verify(customerRepository).find(CUSTOMER_EMAIL);
        verify(customerRepository).update(any(Customer.class));
        verify(quizRepository).update(any(Quiz.class));
        verify(facebookService).sendLeadEvent(CUSTOMER, QUIZ_EXTERNAL_REFERENCE, FACEBOOK_CLICK);
    }

    private Quiz buildQuiz() {
        return new Quiz(QUIZ.getId(), QUIZ.getExternalReference(), QUIZ.getCreationDate(), null, CUSTOMER_ID, null);
    }
}