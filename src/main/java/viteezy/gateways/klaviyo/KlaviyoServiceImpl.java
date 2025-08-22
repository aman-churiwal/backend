package viteezy.gateways.klaviyo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import viteezy.configuration.PaymentConfiguration;
import viteezy.configuration.klaviyo.KlaviyoConfiguration;
import viteezy.configuration.mail.MailConfiguration;
import viteezy.controller.dto.klaviyo.ProductGetResponse;
import viteezy.db.CustomerRepository;
import viteezy.db.klaviyo.KlaviyoRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.domain.blend.Blend;
import viteezy.domain.Customer;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.ingredient.UnitCode;
import viteezy.domain.ingredient.Ingredient;
import viteezy.domain.klaviyo.KlaviyoConstant;
import viteezy.domain.klaviyo.Location;
import viteezy.domain.klaviyo.Product;
import viteezy.domain.klaviyo.event.*;
import viteezy.domain.klaviyo.profile.*;
import viteezy.domain.klaviyo.profile.subscription.*;
import viteezy.domain.quiz.*;
import viteezy.service.IngredientService;
import viteezy.service.blend.BlendIngredientService;
import viteezy.service.blend.BlendService;
import viteezy.service.quiz.*;
import viteezy.traits.EnforcePresenceTrait;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class KlaviyoServiceImpl implements KlaviyoService, EnforcePresenceTrait {

    private static final Logger LOGGER = LoggerFactory.getLogger(KlaviyoService.class);

    public static final String KLAVIYO_API_KEY = "Klaviyo-API-Key";
    public static final String REVISION = "revision";
    public static final String REVISION_DATE = "2025-04-15";

    private static final String EVENT_PATH = "/events";
    private static final String PROFILE_PATH = "/profiles";
    private static final String PROFILE_SUBSCRIPTION_PATH = "/profile-subscription-bulk-create-jobs";

    private final KlaviyoRepository klaviyoRepository;
    private final CustomerRepository customerRepository;
    private final IngredientService ingredientService;
    private final QuizRepository quizRepository;
    private final BlendService blendService;
    private final BlendIngredientService blendIngredientService;

    private final DateOfBirthAnswerService dateOfBirthAnswerService;
    private final UsageGoalService usageGoalService;
    private final UsageGoalAnswerService usageGoalAnswerService;
    private final PrimaryGoalService primaryGoalService;
    private final PrimaryGoalAnswerService primaryGoalAnswerService;
    private final AllergyTypeService allergyTypeService;
    private final AllergyTypeAnswerService allergyTypeAnswerService;
    private final DietTypeService dietTypeService;
    private final DietTypeAnswerService dietTypeAnswerService;

    private final KlaviyoConfiguration klaviyoConfiguration;
    private final MailConfiguration mailConfiguration;
    private final PaymentConfiguration paymentConfiguration;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public KlaviyoServiceImpl(KlaviyoRepository klaviyoRepository, CustomerRepository customerRepository,
                              IngredientService ingredientService, QuizRepository quizRepository,
                              BlendService blendService, BlendIngredientService blendIngredientService,
                              DateOfBirthAnswerService dateOfBirthAnswerService, UsageGoalService usageGoalService,
                              UsageGoalAnswerService usageGoalAnswerService, PrimaryGoalService primaryGoalService,
                              PrimaryGoalAnswerService primaryGoalAnswerService, AllergyTypeService allergyTypeService,
                              AllergyTypeAnswerService allergyTypeAnswerService, DietTypeService dietTypeService,
                              DietTypeAnswerService dietTypeAnswerService, KlaviyoConfiguration klaviyoConfiguration,
                              MailConfiguration mailConfiguration, PaymentConfiguration paymentConfiguration,
                              ObjectMapper objectMapper) {
        this.klaviyoRepository = klaviyoRepository;
        this.customerRepository = customerRepository;
        this.ingredientService = ingredientService;
        this.quizRepository = quizRepository;
        this.blendService = blendService;
        this.blendIngredientService = blendIngredientService;
        this.dateOfBirthAnswerService = dateOfBirthAnswerService;
        this.usageGoalService = usageGoalService;
        this.usageGoalAnswerService = usageGoalAnswerService;
        this.primaryGoalService = primaryGoalService;
        this.primaryGoalAnswerService = primaryGoalAnswerService;
        this.allergyTypeService = allergyTypeService;
        this.allergyTypeAnswerService = allergyTypeAnswerService;
        this.dietTypeService = dietTypeService;
        this.dietTypeAnswerService = dietTypeAnswerService;
        this.klaviyoConfiguration = klaviyoConfiguration;
        this.mailConfiguration = mailConfiguration;
        this.paymentConfiguration = paymentConfiguration;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public Try<List<ProductGetResponse>> getProducts() {
        return klaviyoRepository.findProducts()
                .map(products -> products.stream()
                        .map(this::buildProduct)
                        .collect(Collectors.toList()));
    }

    @Override
    public Try<Customer> upsertInitialProfile(Customer customer, Optional<Quiz> optionalQuiz) {
        if (Optional.ofNullable(customer.getKlaviyoProfileId()).isPresent()) {
            return upsertInitialProfileAsync(customer, optionalQuiz)
                    .map(__ -> customer)
                    .onFailure(peekException());
        } else {
            return upsertInitialProfileSync(customer, optionalQuiz)
                    .flatMap(profile -> customerRepository.update(buildCustomerWithProfileId(customer, profile)))
                    .peek(updatedCustomer -> subscribeProfileAsync(updatedCustomer, klaviyoConfiguration.getNewsletterListId()))
                    .onFailure(peekException());
        }
    }

    @Override
    public Try<Customer> upsertFullProfile(Customer customer, PaymentPlan paymentPlan) {
        if (Optional.ofNullable(customer.getKlaviyoProfileId()).isPresent()) {
            return upsertFullProfileAsync(customer, paymentPlan)
                    .map(__ -> customer)
                    .onFailure(peekException());
        } else {
            return upsertFullProfileSync(customer, paymentPlan)
                    .flatMap(profile -> customerRepository.update(buildCustomerWithProfileId(customer, profile)))
                    .peek(updatedCustomer -> subscribeProfileAsync(updatedCustomer, klaviyoConfiguration.getNewsletterListId()))
                    .onFailure(peekException());
        }
    }

    @Override
    public Try<Void> upsertExistingCustomer(Customer customer, PaymentPlan paymentPlan) {
        return upsertFullProfileSync(customer, paymentPlan)
                .flatMap(profile -> customerRepository.update(buildCustomerWithProfileId(customer, profile)))
                .flatMap(updatedCustomer -> subscribeProfileAsync(updatedCustomer, klaviyoConfiguration.getExistingCustomerListId()))
                .onFailure(peekException());
    }

    @Override
    public Try<Void> upsertExistingInitialProfile(Customer customer, Optional<Quiz> optionalQuiz) {
        return upsertInitialProfileSync(customer, optionalQuiz)
                .flatMap(profile -> customerRepository.update(buildCustomerWithProfileId(customer, profile)))
                .flatMap(updatedCustomer -> subscribeProfileAsync(updatedCustomer, klaviyoConfiguration.getExistingCustomerListId()))
                .onFailure(peekException());
    }

    @Override
    public void createPurchasePaidEvent(Customer customer, PaymentPlan paymentPlan, Payment payment) {
        Try.run(() -> {
            final String body = createPurchasePaidEventBody(customer, paymentPlan, payment);
            final HttpRequest request = buildPostRequest(body, EVENT_PATH);
            final HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            peekResponse(httpResponse);
        }).onFailure(peekException());
    }

    @Override
    public void createEvent(Customer customer, String eventName, String description, Optional<Long> optionalBlendId) {
        Try.run(() -> {
            final String body = createEventBody(customer, eventName, description, optionalBlendId);
            final HttpRequest request = buildPostRequest(body, EVENT_PATH);
            final HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            peekResponse(httpResponse);
        }).onFailure(peekException());
    }

    private Try<Void> subscribeProfileAsync(Customer customer, String listId) {
        return Try.run(() -> {
            final String body = createSubscriptionBody(customer, listId);
            final HttpRequest request = buildPostRequest(body, PROFILE_SUBSCRIPTION_PATH);
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        }).onFailure(peekException());
    }

    private Try<Profile> upsertFullProfileSync(Customer customer, PaymentPlan paymentPlan) {
        return Try.of(() -> {
            final Optional<Quiz> optionalQuiz = blendService.find(paymentPlan.blendId())
                    .flatMap(blend -> quizRepository.find(blend.getQuizId()))
                    .toJavaOptional();
            final String body = createProfileBody(customer, paymentPlan, optionalQuiz);
            final HttpRequest request = buildProfileRequest(customer, body);
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readerFor(Profile.class).readValue(response.body());
        });
    }

    private Try<Void> upsertFullProfileAsync(Customer customer, PaymentPlan paymentPlan) {
        return Try.run(() -> {
            final Optional<Quiz> optionalQuiz = blendService.find(paymentPlan.blendId())
                    .flatMap(blend -> quizRepository.find(blend.getQuizId()))
                    .toJavaOptional();
            final String body = createProfileBody(customer, paymentPlan, optionalQuiz);
            final HttpRequest request = buildProfileRequest(customer, body);
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        }).onFailure(peekException());
    }

    private Try<Profile> upsertInitialProfileSync(Customer customer, Optional<Quiz> optionalQuiz) {
        return Try.of(() -> {
            final String body = createProfileBody(customer, optionalQuiz);
            final HttpRequest request = buildProfileRequest(customer, body);
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readerFor(Profile.class).readValue(response.body());
        });
    }

    private Try<Void> upsertInitialProfileAsync(Customer customer, Optional<Quiz> optionalQuiz) {
        return Try.run(() -> {
            final String body = createProfileBody(customer, optionalQuiz);
            final HttpRequest request = buildProfileRequest(customer, body);
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        });
    }

    private HttpRequest buildProfileRequest(Customer customer, String body) {
        return httpRequestBuilder(customer.getKlaviyoProfileId(), body)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setHeader(REVISION, REVISION_DATE)
                .setHeader(HttpHeaders.AUTHORIZATION, KLAVIYO_API_KEY + StringUtils.SPACE.concat(klaviyoConfiguration.getApiKey()))
                .build();
    }

    private HttpRequest buildPostRequest(String body, String path) {
        return HttpRequest.newBuilder()
                .uri(buildURI(path))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setHeader(REVISION, REVISION_DATE)
                .setHeader(HttpHeaders.AUTHORIZATION, KLAVIYO_API_KEY + StringUtils.SPACE.concat(klaviyoConfiguration.getApiKey()))
                .build();
    }

    private String createProfileBody(Customer customer, Optional<Quiz> optionalQuiz) {
        return Try.of(() -> {
            final ProfileAttributes profileAttributes = getInitialProfileAttributes(customer, optionalQuiz);
            final ProfileData profileData = getProfileData(customer.getKlaviyoProfileId(), profileAttributes);
            return objectMapper.writeValueAsString(new Profile(profileData));
        }).get();
    }

    private String createProfileBody(Customer customer, PaymentPlan paymentPlan, Optional<Quiz> optionalQuiz) {
        return Try.of(() -> {
            final ProfileAttributes profileAttributes = getFullProfileAttributes(customer, paymentPlan, optionalQuiz);
            final ProfileData profileData = getProfileData(customer.getKlaviyoProfileId(), profileAttributes);
            return objectMapper.writeValueAsString(new Profile(profileData));
        }).get();
    }

    private String createSubscriptionBody(Customer customer, String listId) {
        return Try.of(() -> {
            final SubscriptionData subscriptionData = new SubscriptionData(listId, KlaviyoConstant.LIST, null, null);
            final SubscriptionRelationshipList subscriptionRelationshipList = new SubscriptionRelationshipList(subscriptionData);
            final SubscriptionRelationship subscriptionRelationship = new SubscriptionRelationship(subscriptionRelationshipList);
            final ProfileData profileData = new ProfileData(customer.getKlaviyoProfileId(), KlaviyoConstant.PROFILE, new ProfileAttributes(customer.getEmail(), null, null, null, null, null));
            final Profiles profiles = new Profiles(Collections.singletonList(profileData));
            final SubscriptionAttributes subscriptionAttributes = new SubscriptionAttributes(profiles);
            final Subscription subscription = new Subscription(new SubscriptionData(null, KlaviyoConstant.PROFILE_SUBSCRIPTION_BULK_CREATE_JOB, subscriptionAttributes, subscriptionRelationship));
            return objectMapper.writeValueAsString(subscription);
        }).get();
    }

    private ProfileProperties getQuizPropertiesAndIngredients(UUID customerExternalReference, Optional<Quiz> optionalQuiz) {
        final UUID quizExternalReference = optionalQuiz.map(Quiz::getExternalReference).orElse(null);
        final Integer age = dateOfBirthAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .map(dateOfBirthAnswer -> Period.between(dateOfBirthAnswer.getDate(), LocalDate.now()).getYears())
                .getOrElse(() -> null);

        final String primaryGoal = primaryGoalAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(primaryGoalAnswer -> primaryGoalService.find(primaryGoalAnswer.getPrimaryGoalId()))
                .flatMap(optionalToNarrowedEither())
                .map(PrimaryGoal::getName)
                .getOrElse(() -> null);

        final String secondaryGoal = findUsageGoals(quizExternalReference)
                .filter(usageGoal -> !usageGoal.getName().equals(primaryGoal))
                .map(UsageGoal::getName)
                .getOrElse(() -> null);

        final String dietType = dietTypeAnswerService.find(quizExternalReference)
                .flatMap(optionalToNarrowedEither())
                .flatMap(dietTypeAnswer -> dietTypeService.find(dietTypeAnswer.getDietTypeId()))
                .flatMap(optionalToNarrowedEither())
                .map(DietType::getName)
                .getOrElse(() -> null);

        final String allergyTypes = findAllergyTypes(quizExternalReference)
                .map(this::getAllergyTypeNames)
                .getOrElse(() -> null);

        final String blendExternalReference = blendService.findByCustomerExternalReference(customerExternalReference)
                .map(blend -> blend.getExternalReference().toString())
                .getOrElse(() -> null);

        final String ingredients = getIngredients(customerExternalReference);
        return new ProfileProperties(
                age, primaryGoal, secondaryGoal, dietType, allergyTypes, blendExternalReference, ingredients,
                null, null, null, null, null);
    }

    private Seq<UsageGoal> findUsageGoals(UUID quizExternalReference) {
        return usageGoalAnswerService.find(quizExternalReference)
                .map(answers -> answers.stream()
                        .map(answer -> usageGoalService.find(answer.getUsageGoalId())
                                .flatMap(optionalToNarrowedEither()))
                        .collect(Collectors.toList())
                )
                .flatMap(Either::sequenceRight)
                .get();
    }

    private Either<Throwable, Seq<AllergyType>> findAllergyTypes(UUID quizExternalReference) {
        return allergyTypeAnswerService.find(quizExternalReference)
                .map(answers -> answers.stream()
                        .map(answer -> allergyTypeService.find(answer.getAllergyTypeId())
                                .flatMap(optionalToNarrowedEither())
                        ).collect(Collectors.toList())
                )
                .flatMap(Either::sequenceRight);
    }

    private String getAllergyTypeNames(Seq<AllergyType> ingredients) {
        return ingredients
                .map(AllergyType::getName)
                .intersperse(", ")
                .reduceOption(String::concat)
                .getOrElse(StringUtils.EMPTY)
                .trim();
    }

    private String getIngredientNames(Seq<Ingredient> ingredients) {
        return ingredients
                .map(Ingredient::getName)
                .intersperse(", ")
                .reduceOption(String::concat)
                .getOrElse(StringUtils.EMPTY)
                .trim();
    }

    private HttpRequest.Builder httpRequestBuilder(String klaviyoProfileId, String body) {
        if (Optional.ofNullable(klaviyoProfileId).isPresent()) {
            return HttpRequest.newBuilder()
                    .uri(buildURI(PROFILE_PATH.concat("/" + klaviyoProfileId)))
                    .method(RequestMethod.PATCH.name(), HttpRequest.BodyPublishers.ofString(body));
        } else {
            return HttpRequest.newBuilder()
                    .uri(buildURI(PROFILE_PATH))
                    .method(RequestMethod.POST.name(), HttpRequest.BodyPublishers.ofString(body));
        }
    }

    private String createPurchasePaidEventBody(Customer customer, PaymentPlan paymentPlan, Payment payment) {
        return Try.of(() -> {
            final ProfileAttributes profileAttributes = getEventProfileAttributes(customer, getLocation(customer));
            final ProfileData profileData = getProfileData(customer.getKlaviyoProfileId(), profileAttributes);
            final EventProfile profile = new EventProfile(profileData);
            final Metric metric = new Metric(getEventData(new EventAttributes(KlaviyoConstant.PLACED_ORDER), KlaviyoConstant.METRIC));
            final EventProperties eventProperties = new EventProperties(payment.getSequenceType().toString(), getItems(paymentPlan.blendId()));
            final EventAttributes eventAttributes = new EventAttributes(null, eventProperties, getDateTimeFormatted(LocalDateTime.now()), payment.getAmount(), String.valueOf(payment.getId()), metric, profile);
            final EventData eventData = getEventData(eventAttributes, KlaviyoConstant.EVENT);
            return objectMapper.writeValueAsString(new Event(eventData));
        }).get();
    }

    private String createEventBody(Customer customer, String eventName, String description, Optional<Long> optionalBlendId) {
        return Try.of(() -> {
            final ProfileAttributes profileAttributes = getEventProfileAttributes(customer, getLocation(customer));
            final ProfileData profileData = getProfileData(customer.getKlaviyoProfileId(), profileAttributes);
            final EventProfile profile = new EventProfile(profileData);
            final Metric metric = new Metric(getEventData(new EventAttributes(eventName), KlaviyoConstant.METRIC));
            final EventProperties eventProperties = new EventProperties(description, optionalBlendId.map(this::getItems).orElse(null));
            final EventAttributes eventAttributes = new EventAttributes(null, eventProperties, getDateTimeFormatted(LocalDateTime.now()), null, null, metric, profile);
            final EventData eventData = getEventData(eventAttributes, KlaviyoConstant.EVENT);
            return objectMapper.writeValueAsString(new Event(eventData));
        }).get();
    }

    private Location getLocation(Customer customer) {
        return new Location(buildAddress(customer), customer.getCity(), customer.getCountry(), customer.getPostcode());
    }

    private ProfileAttributes getInitialProfileAttributes(Customer customer, Optional<Quiz> optionalQuiz) {
        final ProfileProperties profileProperties = getQuizPropertiesAndIngredients(customer.getExternalReference(), optionalQuiz);
        final Location location = Optional.ofNullable(customer.getStreet()).isPresent() ? getLocation(customer) : null;
        final String lastName = Optional.ofNullable(customer.getLastName()).isPresent() ? customer.getLastName() : null;
        return new ProfileAttributes(customer.getEmail(), customer.getExternalReference().toString(), customer.getFirstName(), lastName, location, profileProperties);
    }

    private ProfileAttributes getEventProfileAttributes(Customer customer, Location location) {
        return new ProfileAttributes(customer.getEmail(), customer.getExternalReference().toString(), customer.getFirstName(), customer.getLastName(), location, null);
    }

    private ProfileAttributes getFullProfileAttributes(Customer customer, PaymentPlan paymentPlan, Optional<Quiz> optionalQuiz) {
        ProfileProperties profileProperties;
        final String blendExternalReference = blendService.find(paymentPlan.blendId())
                .map(blend -> blend.getExternalReference().toString())
                .getOrElse(() -> null);

        if (optionalQuiz.isPresent()) {
            ProfileProperties quizProfileProperties = getQuizPropertiesAndIngredients(customer.getExternalReference(), optionalQuiz);
            profileProperties = new ProfileProperties(
                    quizProfileProperties.getAge(), quizProfileProperties.getPrimaryGoal(),
                    quizProfileProperties.getSecondaryGoal(), quizProfileProperties.getDietType(),
                    quizProfileProperties.getAllergyType(), blendExternalReference,
                    quizProfileProperties.getIngredients(), paymentPlan.status().toString(),
                    paymentPlan.stopReason(), paymentPlan.recurringMonths(),
                    paymentPlan.paymentDate().toLocalDate().toString(), customer.getReferralCode());
        } else {
            profileProperties = new ProfileProperties(null, null, null, null, null,
                    blendExternalReference, getIngredients(customer.getExternalReference()),
                    paymentPlan.status().toString(), paymentPlan.stopReason(), paymentPlan.recurringMonths(),
                    paymentPlan.paymentDate().toLocalDate().toString(), customer.getReferralCode());
        }
        return new ProfileAttributes(customer.getEmail(), customer.getExternalReference().toString(), customer.getFirstName(), customer.getLastName(), getLocation(customer), profileProperties);
    }

    private ProfileData getProfileData(String klaviyoProfileId, ProfileAttributes profileAttributes) {
        return new ProfileData(klaviyoProfileId, KlaviyoConstant.PROFILE, profileAttributes);
    }

    private EventData getEventData(EventAttributes eventAttributes, String event) {
        return new EventData(event, eventAttributes);
    }

    private List<Item> getItems(Long blendId) {
        return blendIngredientService.findByBlendId(blendId)
                .map(blendIngredients -> blendIngredients.stream()
                        .map(blendIngredient -> ingredientService.find(blendIngredient.getIngredientId())
                                .map(ingredient -> new Item(ingredient.getId(), ingredient.getName(), ingredient.getCode(), buildImageUrl(ingredient.getCode()), blendIngredient.getPrice()))
                                .get())
                        .collect(Collectors.toList())
                ).getOrElse(Collections.emptyList());
    }

    private String getIngredients(UUID customerExternalReference) {
        final Try<Blend> blendTry = blendService.findByCustomerExternalReference(customerExternalReference);
        String ingredients;
        if (blendTry.isSuccess()) {
            ingredients = blendTry.flatMap(blend -> getIngredients(blend.getId()))
                    .map(this::getIngredientNames)
                    .getOrElse(() -> null);
        } else {
            ingredients = null;
        }
        return ingredients;
    }

    private Try<Seq<Ingredient>> getIngredients(Long blendId) {
        return blendIngredientService.findByBlendId(blendId)
                .map(blendIngredients -> blendIngredients.stream()
                        .filter(blendIngredient -> !UnitCode.UNITLESS.equals(blendIngredient.getIsUnit()))
                        .map(blendIngredient -> ingredientService.find(blendIngredient.getIngredientId()))
                        .collect(Collectors.toList())
                )
                .flatMap(Try::sequence);
    }

    private Customer buildCustomerWithProfileId(Customer customer, Profile profile) {
        return new Customer(
                customer.getId(), customer.getEmail(), customer.getOptIn(), customer.getExternalReference(),
                customer.getMollieCustomerId(), customer.getActiveCampaignContactId(),
                customer.getActiveCampaignEcomCustomerId(), profile.getProfileData().getId(), customer.getGaId(),
                customer.getFacebookPixel(), customer.getUserAgent(), customer.getUserIpAddress(),
                customer.getFirstName(), customer.getLastName(), customer.getPhoneNumber(), customer.getStreet(),
                customer.getHouseNumber(), customer.getHouseNumberAddition(),customer.getPostcode(),
                customer.getCity(), customer.getCountry(), customer.getReferralCode(), customer.getCreationDate(),
                LocalDateTime.now());
    }

    private ProductGetResponse buildProduct(Product product) {
        return new ProductGetResponse(product.getId(), product.getName(), product.getDescription(),
                buildImageUrl(product.getCode()),
                buildProductUrl(product.getUrl()));
    }

    private String getDateTimeFormatted(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    private URI buildURI(String path) {
        return URI.create(klaviyoConfiguration.getUrl().concat(path));
    }

    private String buildImageUrl(String code) {
        return mailConfiguration.getFrontendBaseUrl().concat("/assets/image/capsules/").concat(code).concat("-big.jpg");
    }

    private String buildProductUrl(String url) {
        return mailConfiguration.getFrontendBaseUrl() + "/" + url;
    }

    private String buildAddress(Customer customer) {
        final String houseNumberAddition = Optional.ofNullable(customer.getHouseNumberAddition()).isPresent() ? "-" + customer.getHouseNumberAddition() : StringUtils.EMPTY;
        return customer.getStreet() + StringUtils.SPACE + customer.getHouseNumber() + houseNumberAddition;
    }

    private boolean isTestMode() {
        return paymentConfiguration.getApiKey().startsWith("test_");
    }

    private void peekResponse(HttpResponse<String> httpResponse) {
        if (isTestMode()) {
            LOGGER.debug("Klaviyo event response code: {} body: {}", httpResponse.statusCode(), httpResponse.body());
        }
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}