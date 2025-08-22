package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.PregnancyStateAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class PregnancyStateAnswerGetResponse {

    private final Long id;
    private final Long pregnancyStateId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public PregnancyStateAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "pregnancyStateId", required = true) Long pregnancyStateId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.pregnancyStateId = pregnancyStateId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getPregnancyStateId() {
        return pregnancyStateId;
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
        PregnancyStateAnswerGetResponse pregnancyStateAnswerGetResponse = (PregnancyStateAnswerGetResponse) o;
        return Objects.equals(id, pregnancyStateAnswerGetResponse.id) &&
                Objects.equals(pregnancyStateId, pregnancyStateAnswerGetResponse.pregnancyStateId) &&
                Objects.equals(creationTimestamp, pregnancyStateAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, pregnancyStateAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pregnancyStateId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PregnancyStateAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("pregnancyStateId='" + pregnancyStateId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static PregnancyStateAnswerGetResponse from(PregnancyStateAnswer that) {
        return new PregnancyStateAnswerGetResponse(that.getId(), that.getPregnancyStateId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
