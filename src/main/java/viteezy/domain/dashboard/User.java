package viteezy.domain.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;

public class User implements Principal {

    private final Long id;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationDate;
    private final UserRoles role;

    public User(Long id, String email, String password, String firstName, String lastName, LocalDateTime creationDate, UserRoles role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.creationDate = creationDate;
        this.role = role;
    }

     public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    @JsonIgnore
    public String getName() {
        return email;
    }

    public UserRoles getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstName + '\'' +
                ", lastname='" + lastName + '\'' +
                ", creationDate=" + creationDate +
                ", role=" + role.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(creationDate, user.creationDate) &&
                Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, firstName, lastName, creationDate, role.toString());
    }
}