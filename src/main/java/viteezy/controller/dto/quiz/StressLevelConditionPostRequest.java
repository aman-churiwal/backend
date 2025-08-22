package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.StressLevelCondition;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class StressLevelConditionPostRequest {

    private final String name;
    private final String code;
    private final Boolean isActive;
    private final String subtitle;

    @JsonCreator
    public StressLevelConditionPostRequest(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "code", required = true) String code,
            @JsonProperty(value = "isActive", required = true) Boolean isActive,
            @JsonProperty(value = "subtitle", required = true) String subtitle
    ) {
        this.name = name;
        this.code = code;
        this.isActive = isActive;
        this.subtitle = subtitle;
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

    public String getSubtitle() {
        return subtitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StressLevelConditionPostRequest that = (StressLevelConditionPostRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(code, that.code) && Objects.equals(isActive, that.isActive) && Objects.equals(subtitle, that.subtitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code, isActive, subtitle);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StressLevelConditionPostRequest.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("isActive=" + isActive)
                .add("subtitle='" + subtitle + "'")
                .toString();
    }

    public StressLevelCondition to() {
        return new StressLevelCondition(null, this.name, this.code, this.isActive, this.subtitle, LocalDateTime.now(), LocalDateTime.now());
    }
}