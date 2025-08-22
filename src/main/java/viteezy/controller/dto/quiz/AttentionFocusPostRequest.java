package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.AttentionFocus;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AttentionFocusPostRequest {

    private final String name;
    private final String code;

    @JsonCreator
    public AttentionFocusPostRequest(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "code", required = true) String code
    ) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttentionFocusPostRequest attentionFocusPostRequest = (AttentionFocusPostRequest) o;
        return Objects.equals(name, attentionFocusPostRequest.name) &&
        Objects.equals(code, attentionFocusPostRequest.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AttentionFocusPostRequest.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .toString();
    }

    public AttentionFocus to() {
        return new AttentionFocus(null, this.name, this.code, LocalDateTime.now(), LocalDateTime.now());
    }
}