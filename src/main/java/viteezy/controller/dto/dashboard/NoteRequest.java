package viteezy.controller.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class NoteRequest {
    private final String message;

    @JsonCreator
    public NoteRequest(
            @JsonProperty(value = "message", required = true) String message
    ) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteRequest that = (NoteRequest) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NoteRequest.class.getSimpleName() + "[", "]")
                .add("message='" + message + "'")
                .toString();
    }
}
