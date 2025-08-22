package viteezy.controller.dto.dashboard;

import java.util.Objects;

public class JwtResponse {

    private final String accessToken;

    private final String tokenType = "Bearer";

    private final Long userId;

    public JwtResponse(String accessToken, Long userId) {
        this.accessToken = accessToken;
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtResponse that = (JwtResponse) o;
        return Objects.equals(accessToken, that.accessToken) && Objects.equals(tokenType, that.tokenType) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, tokenType, userId);
    }
}
