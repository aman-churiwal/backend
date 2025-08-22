package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.AverageSleepingHoursAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AverageSleepingHoursAnswerGetResponse {

    private final Long id;
    private final Long averageSleepingHoursId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public AverageSleepingHoursAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "averageSleepingHoursId", required = true) Long averageSleepingHoursId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.averageSleepingHoursId = averageSleepingHoursId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getAverageSleepingHoursId() {
        return averageSleepingHoursId;
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
        AverageSleepingHoursAnswerGetResponse averageSleepingHoursAnswerGetResponse = (AverageSleepingHoursAnswerGetResponse) o;
        return Objects.equals(id, averageSleepingHoursAnswerGetResponse.id) &&
                Objects.equals(averageSleepingHoursId, averageSleepingHoursAnswerGetResponse.averageSleepingHoursId) &&
                Objects.equals(creationTimestamp, averageSleepingHoursAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, averageSleepingHoursAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, averageSleepingHoursId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AverageSleepingHoursAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("averageSleepingHoursId='" + averageSleepingHoursId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static AverageSleepingHoursAnswerGetResponse from(AverageSleepingHoursAnswer that) {
        return new AverageSleepingHoursAnswerGetResponse(that.getId(), that.getAverageSleepingHoursId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
