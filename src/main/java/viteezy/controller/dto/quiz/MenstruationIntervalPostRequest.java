package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.MenstruationInterval;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class MenstruationIntervalPostRequest {

    private final String name;
    private final String code;

    @JsonCreator
    public MenstruationIntervalPostRequest(
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
        MenstruationIntervalPostRequest menstruationIntervalPostRequest = (MenstruationIntervalPostRequest) o;
        return Objects.equals(name, menstruationIntervalPostRequest.name) &&
        Objects.equals(code, menstruationIntervalPostRequest.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MenstruationIntervalPostRequest.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .toString();
    }

    public MenstruationInterval to() {
        return new MenstruationInterval(null, this.name, this.code, LocalDateTime.now(), LocalDateTime.now());
    }
}