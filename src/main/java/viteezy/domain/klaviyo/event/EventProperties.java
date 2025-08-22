package viteezy.domain.klaviyo.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventProperties {
    private final String description;
    private final List<Item> items;

    public EventProperties(@JsonProperty("description") String description,
                           @JsonProperty("items") List<Item> items) {
        this.description = description;
        this.items = items;
    }

    public String getDescription() {
        return description;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventProperties that = (EventProperties) o;
        return Objects.equals(description, that.description) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, items);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EventProperties.class.getSimpleName() + "[", "]")
                .add("description='" + description + "'")
                .add("items=" + items)
                .toString();
    }
}
