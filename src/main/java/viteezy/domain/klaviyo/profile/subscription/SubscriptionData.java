package viteezy.domain.klaviyo.profile.subscription;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionData {
    private final String id;
    private final String type;
    private final SubscriptionAttributes subscriptionAttributes;
    private final SubscriptionRelationship subscriptionRelationship;

    @JsonCreator
    public SubscriptionData(@JsonProperty("id") String id,
                            @JsonProperty("type") String type,
                            @JsonProperty("attributes") SubscriptionAttributes subscriptionAttributes,
                            @JsonProperty("relationships") SubscriptionRelationship subscriptionRelationship) {
        this.id = id;
        this.type = type;
        this.subscriptionAttributes = subscriptionAttributes;
        this.subscriptionRelationship = subscriptionRelationship;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @JsonGetter(value = "attributes")
    public SubscriptionAttributes getSubscriptionAttributes() {
        return subscriptionAttributes;
    }

    @JsonGetter(value = "relationships")
    public SubscriptionRelationship getSubscriptionRelationship() {
        return subscriptionRelationship;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionData that = (SubscriptionData) o;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(subscriptionAttributes, that.subscriptionAttributes) && Objects.equals(subscriptionRelationship, that.subscriptionRelationship);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, subscriptionAttributes, subscriptionRelationship);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SubscriptionData.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("type='" + type + "'")
                .add("subscriptionAttributes=" + subscriptionAttributes)
                .add("subscriptionRelationship=" + subscriptionRelationship)
                .toString();
    }
}

