package viteezy.domain.klaviyo.profile.subscription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class SubscriptionRelationshipList {
    private final SubscriptionData subscriptionData;

    @JsonCreator
    public SubscriptionRelationshipList(@JsonProperty("data") SubscriptionData subscriptionData) {
        this.subscriptionData = subscriptionData;
    }

    @JsonGetter(value = "data")
    public SubscriptionData getSubscriptionData() {
        return subscriptionData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionRelationshipList that = (SubscriptionRelationshipList) o;
        return Objects.equals(subscriptionData, that.subscriptionData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriptionData);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SubscriptionRelationshipList.class.getSimpleName() + "[", "]")
                .add("subscriptionData=" + subscriptionData)
                .toString();
    }
}
