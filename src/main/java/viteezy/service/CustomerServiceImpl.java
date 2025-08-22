package viteezy.service;

import be.woutschoovaerts.mollie.data.customer.CustomerResponse;
import com.google.common.base.Throwables;
import io.vavr.control.Try;
import net.jodah.failsafe.function.CheckedSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import viteezy.controller.dto.CustomerAddressPostRequest;
import viteezy.controller.dto.CustomerPatchRequest;
import viteezy.controller.dto.quiz.EmailPostRequest;
import viteezy.db.CustomerRepository;
import viteezy.db.quiz.NameAnswerRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.blend.Blend;
import viteezy.domain.Customer;
import viteezy.domain.klaviyo.KlaviyoConstant;
import viteezy.domain.quiz.NameAnswer;
import viteezy.domain.quiz.Quiz;
import viteezy.gateways.facebook.FacebookService;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.service.blend.BlendService;
import viteezy.service.payment.MollieService;
import viteezy.traits.EnforcePresenceTrait;
import viteezy.traits.RetryTrait;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class CustomerServiceImpl implements CustomerService, RetryTrait, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    private final MollieService mollieService;
    private final CustomerRepository customerRepository;
    private final QuizRepository quizRepository;
    private final BlendService blendService;
    private final KlaviyoService klaviyoService;
    private final UtilityService utilityService;
    private final NameAnswerRepository nameAnswerRepository;
    private final FacebookService facebookService;

    protected CustomerServiceImpl(MollieService mollieService, CustomerRepository customerRepository,
                                  QuizRepository quizRepository, BlendService blendService,
                                  KlaviyoService klaviyoService, UtilityService utilityService,
                                  NameAnswerRepository nameAnswerRepository, FacebookService facebookService) {
        this.mollieService = mollieService;
        this.customerRepository = customerRepository;
        this.quizRepository = quizRepository;
        this.blendService = blendService;
        this.klaviyoService = klaviyoService;
        this.utilityService = utilityService;
        this.nameAnswerRepository = nameAnswerRepository;
        this.facebookService = facebookService;
    }

    @Override
    public Try<Customer> find(Long id) {
        return customerRepository.find(id);
    }

    @Override
    public Try<Customer> find(UUID externalReference) {
        return customerRepository.find(externalReference);
    }

    @Override
    public Try<Customer> find(String email) {
        return customerRepository.find(email);
    }

    @Override
    public Try<List<Customer>> findByEmailWithNeedle(String email) {
        return customerRepository.findByEmailWithNeedle(email);
    }

    @Override
    public Try<List<Customer>> findByPhoneNumberWithNeedle(String phoneNumber) {
        return customerRepository.findByPhoneNumberWithNeedle(phoneNumber);
    }

    @Override
    public Try<List<Customer>> findByPostcodeWithNeedle(String phoneNumber) {
        return customerRepository.findByPostcodeWithNeedle(phoneNumber);
    }

    @Override
    public Try<List<Customer>> findByNameWithNeedle(String name) {
        return customerRepository.findByNameWithNeedle(name);
    }

    @Override
    public Try<Customer> findByEmailWithActivePaymentPlan(String email) {
        return customerRepository.findByEmailWithActivePaymentPlan(email);
    }

    @Override
    public Try<Customer> findByBlend(UUID blendExternalReference) {
        return customerRepository.findByBlend(blendExternalReference);
    }

    @Override
    public Try<Customer> findByQuiz(UUID quizExternalReference) {
        return customerRepository.findByQuiz(quizExternalReference);
    }

    @Override
    public Try<Customer> findByPaymentPlanExternalReference(UUID paymentPlanExternalReference) {
        return customerRepository.findByPaymentPlanExternalReference(paymentPlanExternalReference)
                .onFailure(peekException());
    }

    @Override
    public Try<Customer> save(Customer customer) {
        return customerRepository.save(customer)
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    @Override
    public Try<Customer> saveEmailIfNotActive(UUID quizExternalReference, EmailPostRequest emailPostRequest, String gaId, String facebookPixel, String facebookClick, String userAgent, String userIpAddress) {
        return quizRepository.find(quizExternalReference)
                .flatMap(quiz -> updateOrCreateNotActive(quiz, emailPostRequest.getEmail(), emailPostRequest.getOptIn(), gaId, facebookPixel, userAgent, userIpAddress))
                .peek(customer -> sendLeadEvent(customer, quizExternalReference, facebookClick))
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    @Override
    public Try<Customer> update(UUID customerExternalReference, CustomerPatchRequest customerPatchRequest) {
        final Try<Customer> customerTry = customerRepository.findByEmailWithActivePaymentPlan(customerPatchRequest.getEmail());
        if (customerTry.isSuccess()
                && customerPatchRequest.getEmail().equals(customerTry.get().getEmail())
                && !customerExternalReference.equals(customerTry.get().getExternalReference())
        ) {
            return Try.failure(new DuplicateKeyException("Email has active payment plan"));
        } else {
            return customerRepository.find(customerExternalReference)
                    .flatMap(customer -> customerRepository.update(buildCustomerWithContactInformation(customer, customerPatchRequest)))
                    .peek(customer -> klaviyoService.upsertInitialProfile(customer, Optional.empty()));
        }
    }

    @Override
    public Try<Customer> updateEmail(UUID quizExternalReference, EmailPostRequest emailPostRequest, String facebookClick) {
        return quizRepository.find(quizExternalReference)
                .flatMap(quiz -> customerRepository.findByQuiz(quizExternalReference)
                        .map(customer -> buildCustomerWithEmailAndOptIn(customer, emailPostRequest.getEmail(), emailPostRequest.getOptIn()))
                        .flatMap(customerRepository::update)
                        .peek(customer -> klaviyoService.upsertInitialProfile(customer, Optional.of(quiz))))
                .peek(customer -> sendLeadEvent(customer, quizExternalReference, facebookClick))
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    @Override
    public Try<Customer> createCustomer(UUID blendExternalReference,
                                        CustomerAddressPostRequest customerAddressPostRequest, String gaId,
                                        String facebookPixel, String userAgent, String userIpAddress) {
        final String email = customerAddressPostRequest.getEmail();
        return findByBlend(blendExternalReference)
                .filter(customer -> customer.getEmail().equals(email))
                // if email is different from current customer
                .orElse(() -> findByEmailWithActivePaymentPlan(email)
                        .fold(getEmailActiveNotAlreadyExists(email), getCustomerFoundOnEmailActive()))
                // if we can't find a customer with this email create it
                .recoverWith(createCustomerWithName(customerAddressPostRequest.getFirstName(), email, gaId, facebookPixel, userAgent, userIpAddress))
                // only continue if new email is not connected to another active customer/paymentPlan
                .flatMap(customer -> upsertMollieCustomer(customer).map(customerResponse -> buildCustomerWithContactInformation(customer, customerAddressPostRequest, customerResponse.getId(), gaId, facebookPixel, userAgent, userIpAddress)))
                .flatMap(customerRepository::update)
                .flatMap(this::updateReferralCode)
                .peek(customer -> blendService.find(blendExternalReference)
                        .flatMap(blend -> blendService.update(buildBlend(blend, customer.getId(), blend.getQuizId())))
                        .flatMap(blend -> quizRepository.findOptional(blend.getQuizId()))
                        .peek(optionalQuiz -> saveQuizCustomerId(customer, optionalQuiz))
                        .peek(optionalQuiz -> klaviyoService.upsertInitialProfile(customer, optionalQuiz))
                        .onFailure(peekException())
                )
                .onFailure(peekException())
                .onFailure(rollbackTransaction());
    }

    @Override
    public Try<Customer> updateReferralCode(Customer customer) {
        final CheckedSupplier<Try<Customer>> supplier = () -> updateReferralCode(customer.getId(), buildReferralCode(customer.getFirstName()));
        return triggerCallWithRetry(supplier, peekException(), retryPolicy())
                .onFailure(throwable -> LOGGER.error("Failure updating referral code for customerId {}", customer.getId(), throwable));
    }

    private Try<Customer> updateOrCreateNotActive(Quiz quiz, String email, Boolean optIn, String gaId, String facebookPixel, String userAgent, String userIpAddress) {
        return findByEmailWithActivePaymentPlan(email)
                .fold(getEmailActiveNotAlreadyExists(email), getCustomerFoundOnEmailActive())
                .map(customer -> Customer.buildWithPixels(customer, gaId, facebookPixel, userAgent, userIpAddress))
                .peek(customerRepository::update)
                .recoverWith(createCustomerWithoutName(quiz, email, optIn, gaId, facebookPixel, userAgent, userIpAddress))
                .peek(customer -> quizRepository.update(buildQuiz(quiz, customer.getId())));
    }

    private Function<Throwable, Try<Customer>> createCustomerWithoutName(Quiz quiz, String email, Boolean optIn, String gaId, String facebookPixel, String userAgent, String userIpAddress) {
        return throwable -> {
            if (throwable instanceof IllegalStateException) {
                return nameAnswerRepository.find(quiz.getExternalReference())
                        .flatMap(nameAnswer -> save(buildInitialCustomer(nameAnswer.map(NameAnswer::getName).orElse(null), email, optIn, gaId, facebookPixel, userAgent, userIpAddress)))
                        .flatMap(customer -> klaviyoService.upsertInitialProfile(customer, Optional.of(quiz)))
                        .peek(customer -> klaviyoService.createEvent(customer, KlaviyoConstant.QUIZ_STARTED, null, Optional.empty()));
            } else {
                return Try.failure(throwable);
            }
        };
    }

    private Function<Throwable, Try<Customer>> createCustomerWithName(String firstName, String email, String gaId, String facebookPixel, String userAgent, String userIpAddress) {
        return throwable -> {
            if (throwable instanceof IllegalStateException) {
                return save(buildInitialCustomer(firstName, email, true, gaId, facebookPixel, userAgent, userIpAddress));
            } else {
                return Try.failure(throwable);
            }
        };
    }

    private Try<CustomerResponse> upsertMollieCustomer(Customer customer) {
        if (customer.getMollieCustomerId() == null) {
            return mollieService.createCustomer(customer.getEmail());
        } else {
            return mollieService.updateCustomer(customer);
        }
    }

    private void sendLeadEvent(Customer customer, UUID quizExternalReference, String facebookClick) {
        facebookService.sendLeadEvent(customer, quizExternalReference, facebookClick);
    }

    private void saveQuizCustomerId(Customer customer, Optional<Quiz> optionalQuiz) {
        optionalQuiz.ifPresent(quiz -> quizRepository.update(buildQuiz(quiz, customer.getId()))
                .onFailure(peekException()));
    }

    private String buildReferralCode(String firstName) {
        return utilityService.buildReferralCode(firstName);
    }

    private Try<Customer> updateReferralCode(Long customerId, String referralCode) {
        return customerRepository.updateReferralCode(customerId, referralCode);
    }

    private Customer buildInitialCustomer(String firstName, String email, Boolean optIn, String gaId, String facebookPixel, String userAgent, String userIpAddress) {
        return new Customer(null, email, optIn, UUID.randomUUID(), null, null,
                null, null, gaId, facebookPixel, userAgent, userIpAddress, firstName, null, null, null, null, null, null, null, null,
                null, LocalDateTime.now(), LocalDateTime.now());
    }

    private Customer buildCustomerWithContactInformation(Customer customer, CustomerAddressPostRequest request,
                                                         String mollieCustomerId, String gaId, String facebookPixel,
                                                         String userAgent, String userIpAddress) {
        final String email = getEmail(request);
        final String firstName = utilityService.getFirstName(request.getFirstName(), request.getLastName());
        return Customer.buildWithContactInformation(customer, mollieCustomerId, gaId, facebookPixel, userAgent, userIpAddress, firstName, request.getLastName(), request.getPhoneNumber(),
                email, request.getStreet(), request.getHouseNumber(), request.getHouseNumberAddition(),
                request.getPostcode(), request.getCity(), request.getCountry()
        );
    }

    private Customer buildCustomerWithContactInformation(Customer customer, CustomerPatchRequest customerPatchRequest) {
        return new Customer(
                customer.getId(), customerPatchRequest.getEmail(), customer.getOptIn(), customer.getExternalReference(),
                customer.getMollieCustomerId(), customer.getActiveCampaignContactId(),
                customer.getActiveCampaignEcomCustomerId(), customer.getKlaviyoProfileId(), customer.getGaId(), customer.getFacebookPixel(),
                customer.getUserAgent(), customer.getUserIpAddress(), customerPatchRequest.getFirstName(),
                customerPatchRequest.getLastName(), customerPatchRequest.getPhoneNumber(),
                customerPatchRequest.getStreet(), customerPatchRequest.getHouseNumber(),
                customerPatchRequest.getHouseNumberAddition(),customerPatchRequest.getPostcode(),
                customerPatchRequest.getCity(),customerPatchRequest.getCountry(),customer.getReferralCode(),
                customer.getCreationDate(), LocalDateTime.now());
    }

    private Customer buildCustomerWithEmailAndOptIn(Customer customer, String email, Boolean optIn) {
        return new Customer(customer.getId(), email, optIn, customer.getExternalReference(),
                customer.getMollieCustomerId(), customer.getActiveCampaignContactId(),
                customer.getActiveCampaignEcomCustomerId(), customer.getKlaviyoProfileId(), customer.getGaId(),
                customer.getFacebookPixel(), customer.getUserAgent(), customer.getUserIpAddress(),
                customer.getFirstName(), customer.getLastName(), customer.getPhoneNumber(), customer.getStreet(),
                customer.getHouseNumber(), customer.getHouseNumberAddition(), customer.getPostcode(), customer.getCity(),
                customer.getCountry(), customer.getReferralCode(), customer.getCreationDate(), LocalDateTime.now()
        );
    }

    private Quiz buildQuiz(Quiz quiz, Long customerId) {
        return new Quiz(quiz.getId(), quiz.getExternalReference(), quiz.getCreationDate(), null, customerId, quiz.getUtmContent());
    }

    private Blend buildBlend(Blend blend, Long customerId, Long quizId) {
        return new Blend(blend.getId(), blend.getStatus(), blend.getExternalReference(), customerId,
                quizId, blend.getCreationTimestamp(), blend.getModificationTimestamp());
    }

    private String getEmail(CustomerAddressPostRequest request) {
        final String email;
        if (isComMisspelled(request.getEmail())) {
            email = request.getEmail().substring(0, request.getEmail().length() -4).concat(".com");
        } else {
            email = request.getEmail();
        }
        return email;
    }

    private boolean isComMisspelled(String email) {
        return email.endsWith(".cim") ||
                email.endsWith(".cin") ||
                email.endsWith(".ckm") ||
                email.endsWith(".ckn") ||
                email.endsWith(".clm") ||
                email.endsWith(".col") ||
                email.endsWith(".con") ||
                email.endsWith(".cop");
    }

    private Function<Customer, Try<Customer>> getCustomerFoundOnEmailActive() {
        return left -> Try.failure(new DuplicateKeyException(left.getEmail()));
    }

    private Function<Throwable, Try<Customer>> getEmailActiveNotAlreadyExists(String email) {
        return throwable -> find(email);
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }

    private Consumer<Throwable> rollbackTransaction() {
        return throwable -> TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
}
