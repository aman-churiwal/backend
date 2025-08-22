package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.SkinProblemAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SkinProblemAnswerGetResponse {

    private final Long id;
    private final Long skinProblemId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public SkinProblemAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "skinProblemId", required = true) Long skinProblemId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.skinProblemId = skinProblemId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getSkinProblemId() {
        return skinProblemId;
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
        SkinProblemAnswerGetResponse skinProblemAnswerGetResponse = (SkinProblemAnswerGetResponse) o;
        return Objects.equals(id, skinProblemAnswerGetResponse.id) &&
                Objects.equals(skinProblemId, skinProblemAnswerGetResponse.skinProblemId) &&
                Objects.equals(creationTimestamp, skinProblemAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, skinProblemAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, skinProblemId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SkinProblemAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("skinProblemId='" + skinProblemId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static SkinProblemAnswerGetResponse from(SkinProblemAnswer that) {
        return new SkinProblemAnswerGetResponse(that.getId(), that.getSkinProblemId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
