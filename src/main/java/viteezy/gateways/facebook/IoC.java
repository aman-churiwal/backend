package viteezy.gateways.facebook;

import io.dropwizard.core.setup.Environment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.configuration.facebook.FacebookConfiguration;
import viteezy.db.configuration.ConfigurationDatabaseObjectRepository;
import viteezy.db.quiz.GenderAnswerRepository;
import viteezy.db.quiz.QuizRepository;
import viteezy.gateways.slack.SlackService;

@Configuration("facebookGatewayIoC")
public class IoC {

    private final FacebookConfiguration facebookConfiguration;
    private final String frontendBaseUrl;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IoC(
            ViteezyConfiguration viteezyConfiguration
    ) {
        this.facebookConfiguration = viteezyConfiguration.getFacebookConfiguration();
        this.frontendBaseUrl = viteezyConfiguration.getMailConfiguration().getFrontendBaseUrl();
    }

    @Bean("facebookAccessTokenService")
    public FacebookAccessTokenService facebookAccessTokenService(ConfigurationDatabaseObjectRepository configurationDatabaseObjectRepository,
                                                                 @Qualifier("dropwizardEnvironment") Environment environment) {
        return new FacebookAccessTokenServiceImpl(facebookConfiguration, configurationDatabaseObjectRepository, environment.getObjectMapper());
    }

    @Bean("facebookService")
    public FacebookService facebookService(GenderAnswerRepository genderAnswerRepository,
                                           QuizRepository quizRepository,
                                           FacebookClient facebookClient,
                                           SlackService slackService) {
        return new FacebookServiceImpl(
                frontendBaseUrl,
                facebookConfiguration.facebookTestEventCode(),
                genderAnswerRepository,
                quizRepository,
                facebookClient,
                slackService);
    }

    @Bean
    public FacebookClient facebookClient(FacebookAccessTokenService facebookAccessTokenService) {
        return new FacebookClientImpl(facebookConfiguration, facebookAccessTokenService);
    }
}
