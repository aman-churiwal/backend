package viteezy.domain.klaviyo.event;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventData {
    private final String type;
    private final EventAttributes eventAttributes;

    public EventData(@JsonProperty("type") String type,
                     @JsonProperty("attributes") EventAttributes eventAttributes) {
        this.type = type;
        this.eventAttributes = eventAttributes;
    }

    public String getType() {
        return type;
    }

    @JsonGetter(value = "attributes")
    public EventAttributes getEventAttributes() {
        return eventAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventData eventData = (EventData) o;
        return Objects.equals(type, eventData.type) && Objects.equals(eventAttributes, eventData.eventAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, eventAttributes);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EventData.class.getSimpleName() + "[", "]")
                .add("type='" + type + "'")
                .add("eventAttributes=" + eventAttributes)
                .toString();
    }
}

