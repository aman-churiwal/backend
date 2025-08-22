package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.StressLevelAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class StressLevelAnswerGetResponse {

    private final Long id;
    private final Long stressLevelId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public StressLevelAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "stressLevelId", required = true) Long stressLevelId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.stressLevelId = stressLevelId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getStressLevelId() {
        return stressLevelId;
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
        StressLevelAnswerGetResponse stressLevelAnswerGetResponse = (StressLevelAnswerGetResponse) o;
        return Objects.equals(id, stressLevelAnswerGetResponse.id) &&
                Objects.equals(stressLevelId, stressLevelAnswerGetResponse.stressLevelId) &&
                Objects.equals(creationTimestamp, stressLevelAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, stressLevelAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stressLevelId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StressLevelAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("stressLevelId='" + stressLevelId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static StressLevelAnswerGetResponse from(StressLevelAnswer that) {
        return new StressLevelAnswerGetResponse(that.getId(), that.getStressLevelId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
