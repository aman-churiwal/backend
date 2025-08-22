package viteezy.domain.klaviyo.profile;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileData {
    private final String id;
    private final String type;
    private final ProfileAttributes profileAttributes;

    @JsonCreator
    public ProfileData(@JsonProperty("id") String id,
                       @JsonProperty("type") String type,
                       @JsonProperty("attributes") ProfileAttributes profileAttributes) {
        this.id = id;
        this.type = type;
        this.profileAttributes = profileAttributes;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @JsonGetter(value = "attributes")
    public ProfileAttributes getProfileAttributes() {
        return profileAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileData that = (ProfileData) o;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(profileAttributes, that.profileAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, profileAttributes);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ProfileData.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("type='" + type + "'")
                .add("profileAttributes=" + profileAttributes)
                .toString();
    }
}

