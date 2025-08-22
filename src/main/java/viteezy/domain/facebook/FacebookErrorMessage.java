package viteezy.domain.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacebookErrorMessage {
    private final String message;

    public FacebookErrorMessage(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
