package viteezy.configuration.googleanalytics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GoogleAnalyticsConfiguration {
    private final String trackingId;
    private final String measurementId;
    private final String apiSecret;

    @JsonCreator
    public GoogleAnalyticsConfiguration(
            @JsonProperty("tracking_id") String trackingId,
            @JsonProperty("measurement_id") String measurementId,
            @JsonProperty("api_secret") String apiSecret) {
        this.trackingId = trackingId;
        this.measurementId = measurementId;
        this.apiSecret = apiSecret;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public String getMeasurementId() {
        return measurementId;
    }

    public String getApiSecret() {
        return apiSecret;
    }
}
