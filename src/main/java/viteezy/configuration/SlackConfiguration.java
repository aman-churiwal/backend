package viteezy.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SlackConfiguration {

    private final String environment;
    private final String ordersUrl;
    private final String errorsUrl;

    @JsonCreator
    public SlackConfiguration(
            @JsonProperty("environment") String environment,
            @JsonProperty("orders_url") String ordersUrl,
            @JsonProperty("errors_url") String errorsUrl) {
        this.environment = environment;
        this.ordersUrl = ordersUrl;
        this.errorsUrl = errorsUrl;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getOrdersUrl() {
        return ordersUrl;
    }

    public String getErrorsUrl() {
        return errorsUrl;
    }
}
