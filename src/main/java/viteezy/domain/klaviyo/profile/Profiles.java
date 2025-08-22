package viteezy.domain.klaviyo.profile;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class Profiles {

    private final List<ProfileData> profileData;

    @JsonCreator
    public Profiles(@JsonProperty("data") List<ProfileData> profileData) {
        this.profileData = profileData;
    }

    @JsonGetter(value = "data")
    public List<ProfileData> getProfileData() {
        return profileData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profiles profiles = (Profiles) o;
        return Objects.equals(profileData, profiles.profileData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileData);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Profiles.class.getSimpleName() + "[", "]")
                .add("profileData=" + profileData)
                .toString();
    }
}
