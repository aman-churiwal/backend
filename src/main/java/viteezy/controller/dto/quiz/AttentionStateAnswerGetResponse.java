package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.AttentionStateAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AttentionStateAnswerGetResponse {

    private final Long id;
    private final Long attentionStateId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public AttentionStateAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "attentionStateId", required = true) Long attentionStateId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.attentionStateId = attentionStateId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getAttentionStateId() {
        return attentionStateId;
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
        AttentionStateAnswerGetResponse attentionStateAnswerGetResponse = (AttentionStateAnswerGetResponse) o;
        return Objects.equals(id, attentionStateAnswerGetResponse.id) &&
                Objects.equals(attentionStateId, attentionStateAnswerGetResponse.attentionStateId) &&
                Objects.equals(creationTimestamp, attentionStateAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, attentionStateAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attentionStateId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AttentionStateAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("attentionStateId='" + attentionStateId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static AttentionStateAnswerGetResponse from(AttentionStateAnswer that) {
        return new AttentionStateAnswerGetResponse(that.getId(), that.getAttentionStateId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
