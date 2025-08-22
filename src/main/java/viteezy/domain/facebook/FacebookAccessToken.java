package viteezy.domain.facebook;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class FacebookAccessToken {
    private final String accessToken;
    private final String tokenType;
    private final Long expiresIn;
    private final FacebookErrorMessage error;

    @JsonCreator
    public FacebookAccessToken(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("token_type") String tokenType,
            @JsonProperty("expires_in") Long expiresIn,
            @JsonProperty("error") FacebookErrorMessage error) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.error = error;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public FacebookErrorMessage getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookAccessToken)) return false;
        FacebookAccessToken that = (FacebookAccessToken) o;
        return Objects.equals(accessToken, that.accessToken) && Objects.equals(tokenType, that.tokenType) && Objects.equals(expiresIn, that.expiresIn) && Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, tokenType, expiresIn, error);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FacebookAccessToken.class.getSimpleName() + "[", "]")
                .add("accessToken='" + accessToken + "'")
                .add("tokenType='" + tokenType + "'")
                .add("expiresIn=" + expiresIn)
                .add("error=" + error)
                .toString();
    }
}
