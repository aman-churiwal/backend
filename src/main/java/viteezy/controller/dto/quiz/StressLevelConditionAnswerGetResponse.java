package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.StressLevelConditionAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class StressLevelConditionAnswerGetResponse {

    private final Long id;
    private final Long stressLevelConditionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public StressLevelConditionAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "stressLevelConditionId", required = true) Long stressLevelConditionId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.stressLevelConditionId = stressLevelConditionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getStressLevelConditionId() {
        return stressLevelConditionId;
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
        StressLevelConditionAnswerGetResponse stressLevelConditionAnswerGetResponse = (StressLevelConditionAnswerGetResponse) o;
        return Objects.equals(id, stressLevelConditionAnswerGetResponse.id) &&
                Objects.equals(stressLevelConditionId, stressLevelConditionAnswerGetResponse.stressLevelConditionId) &&
                Objects.equals(creationTimestamp, stressLevelConditionAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, stressLevelConditionAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stressLevelConditionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StressLevelConditionAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("stressLevelConditionId='" + stressLevelConditionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static StressLevelConditionAnswerGetResponse from(StressLevelConditionAnswer that) {
        return new StressLevelConditionAnswerGetResponse(that.getId(), that.getStressLevelConditionId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
