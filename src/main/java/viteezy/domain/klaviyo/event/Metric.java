package viteezy.domain.klaviyo.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Metric {
    private final EventData eventData;

    public Metric(@JsonProperty("data") EventData eventData) {
        this.eventData = eventData;
    }

    public EventData getData() {
        return eventData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metric metric = (Metric) o;
        return Objects.equals(eventData, metric.eventData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventData);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Metric.class.getSimpleName() + "[", "]")
                .add("eventData=" + eventData)
                .toString();
    }
}
