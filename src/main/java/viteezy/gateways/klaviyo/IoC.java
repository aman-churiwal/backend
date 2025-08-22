package viteezy.gateways.klaviyo;

import io.dropwizard.core.setup.Environment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.db.CustomerRepository;
import viteezy.db.klaviyo.KlaviyoRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.service.IngredientService;
import viteezy.service.blend.BlendIngredientService;
import viteezy.service.blend.BlendService;
import viteezy.service.quiz.*;

@Configuration("klaviyoGatewayIoC")
public class IoC {

    private final ViteezyConfiguration viteezyConfiguration;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IoC(
            ViteezyConfiguration viteezyConfiguration
    ) {
        this.viteezyConfiguration = viteezyConfiguration;
    }

    @Bean("klaviyoService")
    public KlaviyoService klaviyoService(KlaviyoRepository klaviyoRepository, CustomerRepository customerRepository,
                                         IngredientService ingredientService, QuizRepository quizRepository,
                                         BlendService blendService, BlendIngredientService blendIngredientService,
                                         DateOfBirthAnswerService dateOfBirthAnswerService,
                                         UsageGoalService usageGoalService,
                                         UsageGoalAnswerService usageGoalAnswerService,
                                         PrimaryGoalService primaryGoalService,
                                         PrimaryGoalAnswerService primaryGoalAnswerService,
                                         AllergyTypeService allergyTypeService,
                                         AllergyTypeAnswerService allergyTypeAnswerService,
                                         DietTypeService dietTypeService, DietTypeAnswerService dietTypeAnswerService,
                                         @Qualifier("dropwizardEnvironment") Environment environment) {
        return new KlaviyoServiceImpl(
                klaviyoRepository, customerRepository, ingredientService, quizRepository, blendService, blendIngredientService, dateOfBirthAnswerService, usageGoalService,
                usageGoalAnswerService, primaryGoalService, primaryGoalAnswerService, allergyTypeService,
                allergyTypeAnswerService, dietTypeService, dietTypeAnswerService,
                viteezyConfiguration.getKlaviyoConfiguration(), viteezyConfiguration.getMailConfiguration(),
                viteezyConfiguration.getPaymentConfiguration(), environment.getObjectMapper());
    }
}
