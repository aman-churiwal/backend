package viteezy.domain.klaviyo.profile.subscription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class SubscriptionRelationship {
    private final SubscriptionRelationshipList subscriptionRelationshipList;

    @JsonCreator
    public SubscriptionRelationship(@JsonProperty("list") SubscriptionRelationshipList subscriptionRelationshipList) {
        this.subscriptionRelationshipList = subscriptionRelationshipList;
    }

    @JsonGetter(value = "list")
    public SubscriptionRelationshipList getSubscriptionRelationshipList() {
        return subscriptionRelationshipList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionRelationship that = (SubscriptionRelationship) o;
        return Objects.equals(subscriptionRelationshipList, that.subscriptionRelationshipList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriptionRelationshipList);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SubscriptionRelationship.class.getSimpleName() + "[", "]")
                .add("subscriptionRelationshipList=" + subscriptionRelationshipList)
                .toString();
    }
}
