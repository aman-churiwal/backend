package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.PrimaryGoalAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class PrimaryGoalAnswerGetResponse {

    private final Long id;
    private final Long primaryGoalId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public PrimaryGoalAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "primaryGoalId", required = true) Long primaryGoalId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.primaryGoalId = primaryGoalId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getPrimaryGoalId() {
        return primaryGoalId;
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
        PrimaryGoalAnswerGetResponse primaryGoalAnswerGetResponse = (PrimaryGoalAnswerGetResponse) o;
        return Objects.equals(id, primaryGoalAnswerGetResponse.id) &&
                Objects.equals(primaryGoalId, primaryGoalAnswerGetResponse.primaryGoalId) &&
                Objects.equals(creationTimestamp, primaryGoalAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, primaryGoalAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, primaryGoalId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PrimaryGoalAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("primaryGoalId='" + primaryGoalId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static PrimaryGoalAnswerGetResponse from(PrimaryGoalAnswer that) {
        return new PrimaryGoalAnswerGetResponse(that.getId(), that.getPrimaryGoalId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
