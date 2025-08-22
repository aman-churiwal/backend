package viteezy.gateways.infobip;

import com.infobip.ApiClient;
import com.infobip.ApiKey;
import com.infobip.BaseUrl;
import com.infobip.api.SmsApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.configuration.infobip.InfobipConfiguration;

@Configuration("infobipGatewayIoC")
public class IoC {

    private final InfobipConfiguration InfobipConfiguration;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public IoC(ViteezyConfiguration viteezyConfiguration) {
        this.InfobipConfiguration = viteezyConfiguration.getInfobipConfiguration();
    }

    @Bean("infobipService")
    public InfobipService infobipService() {
        final ApiClient apiClient = ApiClient.forApiKey(ApiKey.from(InfobipConfiguration.getApiKey()))
                .withBaseUrl(BaseUrl.from(InfobipConfiguration.getUrl()))
                .build();
        final SmsApi smsApi = new SmsApi(apiClient);
        return new InfobipServiceImpl(smsApi);
    }
}
