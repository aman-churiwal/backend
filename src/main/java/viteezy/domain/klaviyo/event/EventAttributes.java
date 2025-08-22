package viteezy.domain.klaviyo.event;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventAttributes {
    private final String name;
    private EventProperties eventProperties;
    private String time;
    private BigDecimal value;
    private String uniqueId;
    private Metric metric;
    private EventProfile eventProfile;

    public EventAttributes(@JsonProperty("name") String name,
                           @JsonProperty("properties") EventProperties eventProperties,
                           @JsonProperty("time") String time,
                           @JsonProperty("value") BigDecimal value,
                           @JsonProperty("unique_id") String uniqueId,
                           @JsonProperty("metric") Metric metric,
                           @JsonProperty("profile") EventProfile eventProfile) {
        this.name = name;
        this.eventProperties = eventProperties;
        this.time = time;
        this.value = value;
        this.uniqueId = uniqueId;
        this.metric = metric;
        this.eventProfile = eventProfile;
    }

    public EventAttributes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @JsonGetter(value = "properties")
    public EventProperties getEventProperties() {
        return eventProperties;
    }

    public String getTime() {
        return time;
    }

    public BigDecimal getValue() {
        return value;
    }

    @JsonGetter(value = "unique_id")
    public String getUniqueId() {
        return uniqueId;
    }

    public Metric getMetric() {
        return metric;
    }

    @JsonGetter(value = "profile")
    public EventProfile getEventProfile() {
        return eventProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventAttributes that = (EventAttributes) o;
        return Objects.equals(name, that.name) && Objects.equals(eventProperties, that.eventProperties) && Objects.equals(time, that.time) && Objects.equals(value, that.value) && Objects.equals(uniqueId, that.uniqueId) && Objects.equals(metric, that.metric) && Objects.equals(eventProfile, that.eventProfile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, eventProperties, time, value, uniqueId, metric, eventProfile);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EventAttributes.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("eventProperties=" + eventProperties)
                .add("time='" + time + "'")
                .add("value=" + value)
                .add("uniqueId='" + uniqueId + "'")
                .add("metric=" + metric)
                .add("eventProfile=" + eventProfile)
                .toString();
    }
}
