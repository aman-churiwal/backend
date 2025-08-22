package viteezy.controller.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class LoginPostRequest {

    private final String email;
    private final String password;

    @JsonCreator
    public LoginPostRequest(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password
    ) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginPostRequest that = (LoginPostRequest) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LoginPostRequest.class.getSimpleName() + "[", "]")
                .add("email='" + email + "'")
                .add("password='" + password + "'")
                .toString();
    }
}