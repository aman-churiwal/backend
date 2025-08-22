package viteezy.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Login {

    private final Long id;
    private final String email;
    private final String token;
    private final LocalDateTime lastUpdated;
    private final LocalDateTime creationTimestamp;


    public Login(Long id, String email, String token, LocalDateTime lastUpdated, LocalDateTime creationTimestamp) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.lastUpdated = lastUpdated;
        this.creationTimestamp = creationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return Objects.equals(id, login.id) &&
                Objects.equals(email, login.email) &&
                Objects.equals(token, login.token) &&
                Objects.equals(lastUpdated, login.lastUpdated) &&
                Objects.equals(creationTimestamp, login.creationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, token, lastUpdated, creationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Login.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("email='" + email + "'")
                .add("token='" + token + "'")
                .add("lastUpdated=" + lastUpdated)
                .add("creationTimestamp=" + creationTimestamp)
                .toString();
    }
}
