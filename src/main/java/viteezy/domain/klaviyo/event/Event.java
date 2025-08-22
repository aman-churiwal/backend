package viteezy.domain.klaviyo.event;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class Event {
    private final EventData eventData;

    public Event(@JsonProperty("data") EventData eventData) {
        this.eventData = eventData;
    }

    @JsonGetter(value = "data")
    public EventData getEventData() {
        return eventData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventData, event.eventData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventData);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Event.class.getSimpleName() + "[", "]")
                .add("eventData=" + eventData)
                .toString();
    }
}
