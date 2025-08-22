package viteezy.domain.klaviyo.profile;

import com.fasterxml.jackson.annotation.*;
import viteezy.domain.klaviyo.Location;

import java.util.Objects;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileAttributes {
    private final String email;
    private final String externalId;
    private final String firstName;
    private final String lastName;
    private final Location location;
    private final ProfileProperties profileProperties;

    @JsonCreator
    public ProfileAttributes(@JsonProperty("email") String email,
                             @JsonProperty("external_id") String externalId,
                             @JsonProperty("first_name") String firstName,
                             @JsonProperty("last_name") String lastName,
                             @JsonProperty("location") Location location,
                             @JsonProperty("properties") ProfileProperties profileProperties) {
        this.email = email;
        this.externalId = externalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.profileProperties = profileProperties;
    }

    public String getEmail() {
        return email;
    }

    @JsonGetter(value = "external_id")
    public String getExternalId() {
        return externalId;
    }

    @JsonGetter(value = "first_name")
    public String getFirstName() {
        return firstName;
    }

    @JsonGetter(value = "last_name")
    public String getLastName() {
        return lastName;
    }

    public Location getLocation() {
        return location;
    }

    @JsonGetter(value = "properties")
    public ProfileProperties getProfileProperties() {
        return profileProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileAttributes that = (ProfileAttributes) o;
        return Objects.equals(email, that.email) && Objects.equals(externalId, that.externalId) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(location, that.location) && Objects.equals(profileProperties, that.profileProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, externalId, firstName, lastName, location, profileProperties);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ProfileAttributes.class.getSimpleName() + "[", "]")
                .add("email='" + email + "'")
                .add("externalId='" + externalId + "'")
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("location=" + location)
                .add("profileProperties=" + profileProperties)
                .toString();
    }
}
