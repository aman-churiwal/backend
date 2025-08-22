package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.SleepHoursLessThanSevenAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SleepHoursLessThanSevenAnswerGetResponse {

    private final Long id;
    private final Long sleepHoursLessThanSevenId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public SleepHoursLessThanSevenAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "sleepHoursLessThanSevenId", required = true) Long sleepHoursLessThanSevenId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.sleepHoursLessThanSevenId = sleepHoursLessThanSevenId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getSleepHoursLessThanSevenId() {
        return sleepHoursLessThanSevenId;
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
        SleepHoursLessThanSevenAnswerGetResponse sleepHoursLessThanSevenAnswerGetResponse = (SleepHoursLessThanSevenAnswerGetResponse) o;
        return Objects.equals(id, sleepHoursLessThanSevenAnswerGetResponse.id) &&
                Objects.equals(sleepHoursLessThanSevenId, sleepHoursLessThanSevenAnswerGetResponse.sleepHoursLessThanSevenId) &&
                Objects.equals(creationTimestamp, sleepHoursLessThanSevenAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, sleepHoursLessThanSevenAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sleepHoursLessThanSevenId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SleepHoursLessThanSevenAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("sleepHoursLessThanSevenId='" + sleepHoursLessThanSevenId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static SleepHoursLessThanSevenAnswerGetResponse from(SleepHoursLessThanSevenAnswer that) {
        return new SleepHoursLessThanSevenAnswerGetResponse(that.getId(), that.getSleepHoursLessThanSevenId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
