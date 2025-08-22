package viteezy.configuration.infobip;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InfobipConfiguration {

    private final String url;
    private final String apiKey;

    @JsonCreator
    public InfobipConfiguration(
            @JsonProperty("url") String url,
            @JsonProperty("api_key") String apiKey) {
        this.url = url;
        this.apiKey = apiKey;
    }

    public String getUrl() {
        return url;
    }

    public String getApiKey() {
        return apiKey;
    }
}
