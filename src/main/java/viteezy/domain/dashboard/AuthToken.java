package viteezy.domain.dashboard;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;


public class AuthToken {

    @NotNull
    private final Long id;

    @NotNull
    private final String token;

    @NotNull
    private final Long userId;

    @NotNull
    private final String userRole;

    @NotNull
    private final LocalDateTime lastAccess;

    public AuthToken(Long authTokenId, String token, Long userId, String userRole, LocalDateTime lastAccess) {
        this.id = authTokenId;
        this.token = token;
        this.userId = userId;
        this.userRole = userRole;
        this.lastAccess = lastAccess;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public LocalDateTime getLastAccess() {
        return lastAccess;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", userId=" + userId +
                ", userRole='" + userRole + '\'' +
                ", lastAccess=" + lastAccess +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return Objects.equals(id, authToken.id) && Objects.equals(token, authToken.token) && Objects.equals(userId, authToken.userId) && Objects.equals(userRole, authToken.userRole) && Objects.equals(lastAccess, authToken.lastAccess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, userId, userRole, lastAccess);
    }
}
