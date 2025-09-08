package viteezy.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class TripleWhaleConfiguration {

    @NotEmpty
    @JsonProperty("apiKey")
    private String apiKey;

    @NotEmpty
    @JsonProperty("apiUrl")
    private String apiUrl;

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }
}