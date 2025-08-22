package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.StressLevelAtEndOfDayAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class StressLevelAtEndOfDayAnswerGetResponse {

    private final Long id;
    private final Long stressLevelAtEndOfDayId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public StressLevelAtEndOfDayAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "stressLevelAtEndOfDayId", required = true) Long stressLevelAtEndOfDayId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.stressLevelAtEndOfDayId = stressLevelAtEndOfDayId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getStressLevelAtEndOfDayId() {
        return stressLevelAtEndOfDayId;
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
        StressLevelAtEndOfDayAnswerGetResponse stressLevelAtEndOfDayAnswerGetResponse = (StressLevelAtEndOfDayAnswerGetResponse) o;
        return Objects.equals(id, stressLevelAtEndOfDayAnswerGetResponse.id) &&
                Objects.equals(stressLevelAtEndOfDayId, stressLevelAtEndOfDayAnswerGetResponse.stressLevelAtEndOfDayId) &&
                Objects.equals(creationTimestamp, stressLevelAtEndOfDayAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, stressLevelAtEndOfDayAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stressLevelAtEndOfDayId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StressLevelAtEndOfDayAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("stressLevelAtEndOfDayId='" + stressLevelAtEndOfDayId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static StressLevelAtEndOfDayAnswerGetResponse from(StressLevelAtEndOfDayAnswer that) {
        return new StressLevelAtEndOfDayAnswerGetResponse(that.getId(), that.getStressLevelAtEndOfDayId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
