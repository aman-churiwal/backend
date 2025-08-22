package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.DigestionOccurrence;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DigestionOccurrencePostRequest {

    private final String name;
    private final String code;
    private final Boolean isActive;

    @JsonCreator
    public DigestionOccurrencePostRequest(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "code", required = true) String code,
            @JsonProperty(value = "isActive", required = true) Boolean isActive
    ) {
        this.name = name;
        this.code = code;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Boolean getActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DigestionOccurrencePostRequest that = (DigestionOccurrencePostRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(code, that.code) && Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code, isActive);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DigestionOccurrencePostRequest.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("isActive=" + isActive)
                .toString();
    }

    public DigestionOccurrence to() {
        return new DigestionOccurrence(null, this.name, this.code, this.isActive, LocalDateTime.now(), LocalDateTime.now());
    }
}