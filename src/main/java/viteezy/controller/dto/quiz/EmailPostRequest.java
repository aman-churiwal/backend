package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class EmailPostRequest {

    private final String email;
    private final Boolean optIn;
    private final String fbclid;

    @JsonCreator
    public EmailPostRequest(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "optIn", required = true) Boolean optIn,
            @JsonProperty(value = "fbclid", required = false) String fbclid
    ) {
        this.email = email;
        this.optIn = optIn;
        this.fbclid = fbclid;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getOptIn() {
        return optIn;
    }

    public String getFbclid() {
        return fbclid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailPostRequest)) return false;
        EmailPostRequest that = (EmailPostRequest) o;
        return Objects.equals(email, that.email) && Objects.equals(optIn, that.optIn) && Objects.equals(fbclid, that.fbclid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, optIn, fbclid);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EmailPostRequest.class.getSimpleName() + "[", "]")
                .add("email='" + email + "'")
                .add("optIn=" + optIn)
                .add("fbclid='" + fbclid + "'")
                .toString();
    }
}