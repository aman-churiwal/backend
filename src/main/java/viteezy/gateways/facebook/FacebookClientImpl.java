package viteezy.gateways.facebook;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.serverside.Event;
import com.facebook.ads.sdk.serverside.EventRequest;
import com.facebook.ads.sdk.serverside.EventResponse;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viteezy.configuration.facebook.FacebookConfiguration;

public class FacebookClientImpl implements FacebookClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacebookClient.class);

    private final Long pixelId;
    private final String appSecret;
    private final String facebookTestEventCode;

    private final FacebookAccessTokenService facebookAccessTokenService;
    private final boolean testMode;

    protected FacebookClientImpl(FacebookConfiguration configuration, FacebookAccessTokenService facebookAccessTokenService) {
        this.pixelId = configuration.pixelId();
        this.appSecret = configuration.appSecret();
        this.facebookTestEventCode = configuration.facebookTestEventCode();
        this.facebookAccessTokenService = facebookAccessTokenService;
        this.testMode = StringUtils.isNotBlank(facebookTestEventCode);
    }

    @Override
    public Try<Void> sendEventRequest(Event event) {
        return getApiContext().flatMap(apiContext -> Try.run(() -> {
            final EventRequest eventRequest = new EventRequest(pixelId.toString(), apiContext)
                    .addDataItem(event);
            if (testMode) {
                eventRequest.setTestEventCode(facebookTestEventCode);
            }
            LOGGER.debug("Sending event request containing payload={}", eventRequest.getSerializedPayload());
            final EventResponse eventResponse = eventRequest.execute();
            if (testMode) {
                LOGGER.debug("Facebook eventResponse={}", eventResponse);
            }
        }));
    }

    private Try<APIContext> getApiContext() {
        return facebookAccessTokenService.find()
                .map(facebookAccessToken -> new APIContext(facebookAccessToken.getValue(), appSecret));

    }
}
