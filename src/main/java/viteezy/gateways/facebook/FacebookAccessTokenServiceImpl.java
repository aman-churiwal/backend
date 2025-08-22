package viteezy.gateways.facebook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import io.vavr.control.Try;
import jakarta.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import viteezy.configuration.facebook.FacebookConfiguration;
import viteezy.db.configuration.ConfigurationDatabaseObjectRepository;
import viteezy.domain.ConfigurationDatabaseName;
import viteezy.domain.ConfigurationDatabaseObject;
import viteezy.domain.facebook.FacebookAccessToken;

import javax.naming.AuthenticationException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.function.Consumer;

public class FacebookAccessTokenServiceImpl implements FacebookAccessTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacebookAccessTokenService.class);
    private static final String FB_EXCHANGE_TOKEN = "fb_exchange_token";

    private final FacebookConfiguration facebookConfiguration;
    private final ConfigurationDatabaseObjectRepository configurationDatabaseObjectRepository;
    private final ObjectMapper objectMapper;
    private final UriBuilder facebookGraphBaseUri;

    protected FacebookAccessTokenServiceImpl(FacebookConfiguration facebookConfiguration,
                                          ConfigurationDatabaseObjectRepository configurationDatabaseObjectRepository,
                                          ObjectMapper objectMapper) {
        this.facebookConfiguration = facebookConfiguration;
        this.configurationDatabaseObjectRepository = configurationDatabaseObjectRepository;
        this.objectMapper = objectMapper;
        this.facebookGraphBaseUri = UriBuilder.fromUri("https://graph.facebook.com/" + facebookConfiguration.graphVersion() + "/oauth/access_token");
    }

    @Override
    public Try<ConfigurationDatabaseObject> find() {
        return configurationDatabaseObjectRepository.find(ConfigurationDatabaseName.facebook_access_token)
                .peek(this::generateFacebookAccessTokenIfAlmostExpired);
    }

    private void generateFacebookAccessTokenIfAlmostExpired(ConfigurationDatabaseObject configurationDatabaseObject) {
        // graceful failure, give developers 3 days time to fix this
        if (isDaysBeforeExpirationTimestamp(configurationDatabaseObject.getExpirationTimestamp())) {
            generateFacebookAccessToken(configurationDatabaseObject)
                    .map(this::buildConfigurationDatabaseObject)
                    .flatMap(configurationDatabaseObjectRepository::update)
                    .onFailure(peekException());
        }
    }

    private boolean isDaysBeforeExpirationTimestamp(LocalDateTime expirationTimestamp) {
        return LocalDateTime.now().isAfter(expirationTimestamp.minusDays(3));
    }

    private Try<FacebookAccessToken> generateFacebookAccessToken(ConfigurationDatabaseObject configurationDatabaseObject) {
        return Try.of(() -> {
            final URI uri = facebookGraphBaseUri
                    .queryParam("grant_type", FB_EXCHANGE_TOKEN)
                    .queryParam("client_id", facebookConfiguration.appId())
                    .queryParam("client_secret", facebookConfiguration.appSecret())
                    .queryParam(FB_EXCHANGE_TOKEN, configurationDatabaseObject.getValue())
                    .build();

            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
        }).flatMap(this::buildFacebookAccessToken);
    }

    private Try<FacebookAccessToken> buildFacebookAccessToken(String body) {
        return Try.of(() -> objectMapper.readValue(body, FacebookAccessToken.class))
                .filter(facebookAccessToken -> facebookAccessToken.getError() == null, facebookAccessToken -> new AuthenticationException(facebookAccessToken.getError().getMessage()));
    }

    private ConfigurationDatabaseObject buildConfigurationDatabaseObject(FacebookAccessToken facebookAccessToken) {
        return new ConfigurationDatabaseObject(null, ConfigurationDatabaseName.facebook_access_token.name(), facebookAccessToken.getTokenType(), facebookAccessToken.getAccessToken(), getExpirationTimestamp(facebookAccessToken.getExpiresIn()), LocalDateTime.now(), LocalDateTime.now());
    }

    private LocalDateTime getExpirationTimestamp(@NonNull Long expiresIn) {
        return LocalDateTime.ofEpochSecond(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + expiresIn, 0, ZoneOffset.UTC);
    }

    private Consumer<Throwable> peekException() {
        return throwable -> LOGGER.error("throwable={}", Throwables.getStackTraceAsString(throwable));
    }
}
