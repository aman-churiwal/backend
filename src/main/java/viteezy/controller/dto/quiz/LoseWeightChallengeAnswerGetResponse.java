package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.LoseWeightChallengeAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class LoseWeightChallengeAnswerGetResponse {

    private final Long id;
    private final Long loseWeightChallengeId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public LoseWeightChallengeAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "loseWeightChallengeId", required = true) Long loseWeightChallengeId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.loseWeightChallengeId = loseWeightChallengeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getLoseWeightChallengeId() {
        return loseWeightChallengeId;
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
        LoseWeightChallengeAnswerGetResponse loseWeightChallengeAnswerGetResponse = (LoseWeightChallengeAnswerGetResponse) o;
        return Objects.equals(id, loseWeightChallengeAnswerGetResponse.id) &&
                Objects.equals(loseWeightChallengeId, loseWeightChallengeAnswerGetResponse.loseWeightChallengeId) &&
                Objects.equals(creationTimestamp, loseWeightChallengeAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, loseWeightChallengeAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, loseWeightChallengeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LoseWeightChallengeAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("loseWeightChallengeId='" + loseWeightChallengeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static LoseWeightChallengeAnswerGetResponse from(LoseWeightChallengeAnswer that) {
        return new LoseWeightChallengeAnswerGetResponse(that.getId(), that.getLoseWeightChallengeId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
