package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.SkinType;

import java.time.LocalDateTime;
import java.util.Objects;

public class SkinTypePostRequest {

    private final String name;
    private final String code;
    private final Boolean isActive;
    private final String subtitle;

    @JsonCreator
    public SkinTypePostRequest(
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
        SkinTypePostRequest that = (SkinTypePostRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(code, that.code) && Objects.equals(isActive, that.isActive) && Objects.equals(subtitle, that.subtitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code, isActive, subtitle);
    }

    public SkinType to() {
        return new SkinType(null, this.name, this.code, this.isActive, this.subtitle, LocalDateTime.now(), LocalDateTime.now());
    }
}