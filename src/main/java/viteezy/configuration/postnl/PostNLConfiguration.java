package viteezy.configuration.postnl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PostNLConfiguration {

    private final String url;
    private final String apiKey;
    private final String shipmentApiKey;

    @JsonCreator
    public PostNLConfiguration(
            @JsonProperty("url") String url,
            @JsonProperty("api_key") String apiKey,
            @JsonProperty("shipment_api_key") String shipmentApiKey) {
        this.url = url;
        this.apiKey = apiKey;
        this.shipmentApiKey = shipmentApiKey;
    }

    public String getUrl() {
        return url;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getShipmentApiKey() {
        return shipmentApiKey;
    }
}
