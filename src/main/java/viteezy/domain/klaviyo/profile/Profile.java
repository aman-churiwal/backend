package viteezy.domain.klaviyo.profile;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class Profile {

    private final ProfileData profileData;

    @JsonCreator
    public Profile(@JsonProperty("data") ProfileData profileData) {
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
        Profile profile = (Profile) o;
        return Objects.equals(profileData, profile.profileData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileData);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Profile.class.getSimpleName() + "[", "]")
                .add("profileData=" + profileData)
                .toString();
    }
}
