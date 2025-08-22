package viteezy.gateways.slack;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import viteezy.configuration.ViteezyConfiguration;

@EnableAsync
@Configuration("slackGatewayIoC")
public class IoC {

    private final ViteezyConfiguration configuration;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IoC(
            ViteezyConfiguration viteezyConfiguration
    ) {
        this.configuration = viteezyConfiguration;
    }

    @Bean("slackService")
    public SlackService slackService() {
        return new SlackServiceImpl(configuration.getSlackConfiguration());
    }
}
