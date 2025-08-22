package viteezy.configuration.klaviyo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KlaviyoConfiguration {
    private final String url;
    private final String apiKey;
    private final String newsletterListId;
    private final String existingCustomerListId;

    @JsonCreator
    public KlaviyoConfiguration(
            @JsonProperty("url") String url,
            @JsonProperty("api_key") String apiKey,
            @JsonProperty("newsletter_list_id") String newsletterListId,
            @JsonProperty("existing_customer_list_id") String existingCustomerListId) {
        this.url = url;
        this.apiKey = apiKey;
        this.newsletterListId = newsletterListId;
        this.existingCustomerListId = existingCustomerListId;
    }

    public String getUrl() {
        return url;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getNewsletterListId() {
        return newsletterListId;
    }

    public String getExistingCustomerListId() {
        return existingCustomerListId;
    }
}
