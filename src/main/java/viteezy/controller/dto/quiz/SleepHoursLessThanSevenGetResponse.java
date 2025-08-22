package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.SleepHoursLessThanSeven;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SleepHoursLessThanSevenGetResponse {

    private final Long id;
    private final String name;
    private final String code;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public SleepHoursLessThanSevenGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "code", required = true) String code,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public LocalDateTime getModificationTimestamp() {
        return modificationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SleepHoursLessThanSevenGetResponse sleepHoursLessThanSevenGetResponse = (SleepHoursLessThanSevenGetResponse) o;
        return Objects.equals(id, sleepHoursLessThanSevenGetResponse.id) &&
                Objects.equals(name, sleepHoursLessThanSevenGetResponse.name) &&
                Objects.equals(code, sleepHoursLessThanSevenGetResponse.code) &&
                Objects.equals(creationTimestamp, sleepHoursLessThanSevenGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, sleepHoursLessThanSevenGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SleepHoursLessThanSevenGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static SleepHoursLessThanSevenGetResponse from(SleepHoursLessThanSeven that) {
        return new SleepHoursLessThanSevenGetResponse(that.getId(), that.getName(), that.getCode(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
