package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.AttentionFocusAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AttentionFocusAnswerGetResponse {

    private final Long id;
    private final Long attentionFocusId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public AttentionFocusAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "attentionFocusId", required = true) Long attentionFocusId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.attentionFocusId = attentionFocusId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getAttentionFocusId() {
        return attentionFocusId;
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
        AttentionFocusAnswerGetResponse attentionFocusAnswerGetResponse = (AttentionFocusAnswerGetResponse) o;
        return Objects.equals(id, attentionFocusAnswerGetResponse.id) &&
                Objects.equals(attentionFocusId, attentionFocusAnswerGetResponse.attentionFocusId) &&
                Objects.equals(creationTimestamp, attentionFocusAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, attentionFocusAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attentionFocusId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AttentionFocusAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("attentionFocusId='" + attentionFocusId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static AttentionFocusAnswerGetResponse from(AttentionFocusAnswer that) {
        return new AttentionFocusAnswerGetResponse(that.getId(), that.getAttentionFocusId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
