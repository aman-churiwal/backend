package viteezy.configuration.facebook;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FacebookConfiguration(String facebookTestEventCode, Long pixelId, String appId, String appSecret, String graphVersion) {

    @JsonCreator
    public FacebookConfiguration(
            @JsonProperty("test_event_code") String facebookTestEventCode,
            @JsonProperty("pixel_id") Long pixelId,
            @JsonProperty("app_id") String appId,
            @JsonProperty("app_secret") String appSecret,
            @JsonProperty("graph_version") String graphVersion) {
        this.facebookTestEventCode = facebookTestEventCode;
        this.pixelId = pixelId;
        this.appId = appId;
        this.appSecret = appSecret;
        this.graphVersion = graphVersion;
    }
}
