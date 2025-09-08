package viteezy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import viteezy.configuration.TripleWhaleConfiguration;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.db.*;
import viteezy.db.quiz.NameAnswerRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.gateways.facebook.FacebookService;
import viteezy.gateways.klaviyo.KlaviyoService;
import viteezy.service.blend.BlendService;
import viteezy.service.mail.EmailService;
import viteezy.service.payment.MollieService;
import viteezy.service.pricing.IngredientPriceService;

@Configuration("servicesConfig")
@Import({
        viteezy.db.IoC.class
})
public class IoC {

    private final ViteezyConfiguration viteezyConfiguration;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IoC(
            ViteezyConfiguration viteezyConfiguration
    ) {
        this.viteezyConfiguration = viteezyConfiguration;
    }

    @Bean("bundleService")
    public BundleService bundleService(IngredientService ingredientService, IngredientPriceService ingredientPriceService,
                                       BundleRepository bundleRepository) {
        return new BundleServiceImpl(ingredientService, ingredientPriceService, bundleRepository);
    }

    @Bean("contentService")
    public ContentService contentService(ContentRepository contentRepository) {
        return new ContentServiceImpl(contentRepository);
    }

    @Bean("ingredientService")
    public IngredientService ingredientService(
            IngredientRepository ingredientRepository, IngredientPriceService ingredientPriceService,
            IngredientUnitService ingredientUnitService
    ) {
        return new IngredientServiceImpl(ingredientRepository, ingredientPriceService, ingredientUnitService);
    }

    @Bean("ingredientUnitService")
    public IngredientUnitService ingredientUnitService(
            IngredientUnitRepository ingredientUnitRepository
    ) {
        return new IngredientUnitServiceImpl(ingredientUnitRepository);
    }

    @Bean("loggingService")
    public LoggingService loggingService(CustomerRepository customerRepository, LoggingRepository loggingRepository) {
        return new LoggingServiceImpl(customerRepository, loggingRepository);
    }

    @Bean("loginService")
    public LoginService loginService(
            LoginRepository loginRepository, EmailService emailService,
            CustomerService customerService, LoggingService loggingService
    ) {
        String frontendBaseUrl = viteezyConfiguration.getMailConfiguration().getFrontendBaseUrl();
        return new LoginServiceImpl(loginRepository, emailService, customerService, loggingService, frontendBaseUrl);
    }

    @Bean("reviewService")
    public ReviewService reviewService(ReviewRepository reviewRepository) {
        return new ReviewServiceImpl(reviewRepository);
    }

    @Bean("tripleWhaleService")
    public TripleWhaleService tripleWhaleService() {
        return new TripleWhaleServiceImpl(this.viteezyConfiguration.getTripleWhaleConfiguration());
    }

    @Bean("utilityService")
    public UtilityService utilityService() {
        return new UtilityServiceImpl();
    }

    @Bean("customerService")
    public CustomerService customerService(MollieService mollieService, CustomerRepository customerRepository,
                                           QuizRepository quizRepository, BlendService blendService,
                                           KlaviyoService klaviyoService, UtilityService utilityService,
                                           NameAnswerRepository nameAnswerRepository, FacebookService facebookService) {
        return new CustomerServiceImpl(
                mollieService, customerRepository, quizRepository, blendService, klaviyoService, utilityService,
                nameAnswerRepository, facebookService);
    }
}
