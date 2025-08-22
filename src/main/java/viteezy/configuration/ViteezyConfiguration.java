package viteezy.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import viteezy.configuration.cache.CacheConfiguration;
import viteezy.configuration.cache.CachingDependentConfiguration;
import viteezy.configuration.dashboard.JwtConfiguration;
import viteezy.configuration.facebook.FacebookConfiguration;
import viteezy.configuration.googleanalytics.GoogleAnalyticsConfiguration;
import viteezy.configuration.infobip.InfobipConfiguration;
import viteezy.configuration.klaviyo.KlaviyoConfiguration;
import viteezy.configuration.mail.MailConfiguration;
import viteezy.configuration.postnl.PostNLConfiguration;
import viteezy.configuration.referral.ReferralConfiguration;

public class ViteezyConfiguration extends Configuration implements CachingDependentConfiguration {

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    @Valid
    @NotNull
    @JsonProperty("database")
    private final DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    @JsonProperty("cache")
    private CacheConfiguration cacheConfiguration;

    @Valid
    @NotNull
    @JsonProperty("payment")
    private PaymentConfiguration paymentConfiguration;

    @Valid
    @NotNull
    @JsonProperty("slack")
    private SlackConfiguration slackConfiguration;

    @Valid
    @NotNull
    @JsonProperty("referral")
    private ReferralConfiguration referralConfiguration;

    @Valid
    @NotNull
    @JsonProperty("googleanalytics")
    private GoogleAnalyticsConfiguration googleAnalyticsConfiguration;

    @Valid
    @NotNull
    @JsonProperty("facebook")
    private FacebookConfiguration facebookConfiguration;

    @Valid
    @NotNull
    @JsonProperty("mail")
    private MailConfiguration mailConfiguration;

    @Valid
    @NotNull
    @JsonProperty("fulfilment")
    private FulfilmentConfiguration fulfilmentConfiguration;

    @Valid
    @NotNull
    @JsonProperty("postnl")
    private PostNLConfiguration postNLConfiguration;

    @JsonProperty("jwt")
    @Valid
    @NotNull
    private JwtConfiguration jwtConfiguration;

    @JsonProperty("infobip")
    @Valid
    @NotNull
    private InfobipConfiguration InfobipConfiguration;

    @JsonProperty("klaviyo")
    @Valid
    @NotNull
    private KlaviyoConfiguration klaviyoConfiguration;

    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        return swaggerBundleConfiguration;
    }

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @Override
    public CacheConfiguration getCacheConfiguration() {
        return cacheConfiguration;
    }

    public PaymentConfiguration getPaymentConfiguration() {
        return paymentConfiguration;
    }

    public SlackConfiguration getSlackConfiguration() {
        return slackConfiguration;
    }

    public ReferralConfiguration getReferralConfiguration() {
        return referralConfiguration;
    }

    public GoogleAnalyticsConfiguration getGoogleAnalyticsConfiguration() {
        return googleAnalyticsConfiguration;
    }

    public FacebookConfiguration getFacebookConfiguration() { return  facebookConfiguration; }

    public MailConfiguration getMailConfiguration() {
        return mailConfiguration;
    }

    public FulfilmentConfiguration getFulfilmentConfiguration() {
        return fulfilmentConfiguration;
    }

    public PostNLConfiguration getPostNLConfiguration() {
        return postNLConfiguration;
    }

    public JwtConfiguration getJwtConfiguration() {
        return jwtConfiguration;
    }

    public InfobipConfiguration getInfobipConfiguration() {
        return InfobipConfiguration;
    }

    public KlaviyoConfiguration getKlaviyoConfiguration() {
        return klaviyoConfiguration;
    }
}
