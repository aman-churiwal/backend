package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.IronPrescribedAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class IronPrescribedAnswerGetResponse {

    private final Long id;
    private final Long ironPrescribedId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public IronPrescribedAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "ironPrescribedId", required = true) Long ironPrescribedId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.ironPrescribedId = ironPrescribedId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getIronPrescribedId() {
        return ironPrescribedId;
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
        IronPrescribedAnswerGetResponse ironPrescribedAnswerGetResponse = (IronPrescribedAnswerGetResponse) o;
        return Objects.equals(id, ironPrescribedAnswerGetResponse.id) &&
                Objects.equals(ironPrescribedId, ironPrescribedAnswerGetResponse.ironPrescribedId) &&
                Objects.equals(creationTimestamp, ironPrescribedAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, ironPrescribedAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ironPrescribedId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IronPrescribedAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("ironPrescribedId='" + ironPrescribedId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static IronPrescribedAnswerGetResponse from(IronPrescribedAnswer that) {
        return new IronPrescribedAnswerGetResponse(that.getId(), that.getIronPrescribedId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
