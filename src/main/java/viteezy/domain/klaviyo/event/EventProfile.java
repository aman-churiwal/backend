package viteezy.domain.klaviyo.event;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.klaviyo.profile.ProfileData;

import java.util.Objects;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventProfile {

    private final ProfileData profileData;

    public EventProfile(@JsonProperty("data") ProfileData profileData) {
        this.profileData = profileData;
    }

    @JsonGetter(value = "data")
    public ProfileData getProfileData() {
        return profileData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventProfile that = (EventProfile) o;
        return Objects.equals(profileData, that.profileData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileData);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EventProfile.class.getSimpleName() + "[", "]")
                .add("profileData=" + profileData)
                .toString();
    }
}
