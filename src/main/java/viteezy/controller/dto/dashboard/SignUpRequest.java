package viteezy.controller.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class SignUpRequest {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;

    @JsonCreator
    public SignUpRequest(@JsonProperty("firstname") String firstName,
                         @JsonProperty("lastname") String lastName,
                         @JsonProperty("email") String email,
                         @JsonProperty("password") String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "name='" + firstName + '\'' +
                ", username='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignUpRequest that = (SignUpRequest) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password);
    }
}
