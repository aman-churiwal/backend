package viteezy.gateways.postnl;

import io.dropwizard.core.setup.Environment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.configuration.postnl.PostNLConfiguration;

@Configuration("postNlGatewayIoC")
public class IoC {

    private final PostNLConfiguration postNlConfiguration;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IoC(ViteezyConfiguration viteezyConfiguration) {
        this.postNlConfiguration = viteezyConfiguration.getPostNLConfiguration();
    }

    @Bean("postNlService")
    public PostNlService postNlService(@Qualifier("dropwizardEnvironment") Environment environment) {
        return new PostNlServiceImpl(postNlConfiguration, environment.getObjectMapper());
    }
}
