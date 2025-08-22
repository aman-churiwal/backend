package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.TransitionPeriodComplaints;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class TransitionPeriodComplaintsPostRequest {

    private final String name;
    private final String code;

    @JsonCreator
    public TransitionPeriodComplaintsPostRequest(
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
        TransitionPeriodComplaintsPostRequest transitionPeriodComplaintsPostRequest = (TransitionPeriodComplaintsPostRequest) o;
        return Objects.equals(name, transitionPeriodComplaintsPostRequest.name) &&
        Objects.equals(code, transitionPeriodComplaintsPostRequest.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransitionPeriodComplaintsPostRequest.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .toString();
    }

    public TransitionPeriodComplaints to() {
        return new TransitionPeriodComplaints(null, this.name, this.code, LocalDateTime.now(), LocalDateTime.now());
    }
}