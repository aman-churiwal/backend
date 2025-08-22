package viteezy.domain.klaviyo.profile.subscription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.klaviyo.profile.Profiles;

import java.util.Objects;
import java.util.StringJoiner;

public class SubscriptionAttributes {
    private final Profiles profiles;

    @JsonCreator
    public SubscriptionAttributes(@JsonProperty("profiles") Profiles profiles) {
        this.profiles = profiles;
    }

    public Profiles getProfiles() {
        return profiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionAttributes that = (SubscriptionAttributes) o;
        return Objects.equals(profiles, that.profiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profiles);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SubscriptionAttributes.class.getSimpleName() + "[", "]")
                .add("profiles=" + profiles)
                .toString();
    }
}
