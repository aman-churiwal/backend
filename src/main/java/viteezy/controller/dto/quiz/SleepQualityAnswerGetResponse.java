package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.SleepQualityAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SleepQualityAnswerGetResponse {

    private final Long id;
    private final Long sleepQualityId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public SleepQualityAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "sleepQualityId", required = true) Long sleepQualityId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.sleepQualityId = sleepQualityId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getSleepQualityId() {
        return sleepQualityId;
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
        SleepQualityAnswerGetResponse sleepQualityAnswerGetResponse = (SleepQualityAnswerGetResponse) o;
        return Objects.equals(id, sleepQualityAnswerGetResponse.id) &&
                Objects.equals(sleepQualityId, sleepQualityAnswerGetResponse.sleepQualityId) &&
                Objects.equals(creationTimestamp, sleepQualityAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, sleepQualityAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sleepQualityId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SleepQualityAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("sleepQualityId='" + sleepQualityId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static SleepQualityAnswerGetResponse from(SleepQualityAnswer that) {
        return new SleepQualityAnswerGetResponse(that.getId(), that.getSleepQualityId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
