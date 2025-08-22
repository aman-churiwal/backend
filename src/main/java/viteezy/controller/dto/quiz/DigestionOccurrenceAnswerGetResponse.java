package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.DigestionOccurrenceAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DigestionOccurrenceAnswerGetResponse {

    private final Long id;
    private final Long digestionOccurrenceId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public DigestionOccurrenceAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "digestionOccurrenceId", required = true) Long digestionOccurrenceId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.digestionOccurrenceId = digestionOccurrenceId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getDigestionOccurrenceId() {
        return digestionOccurrenceId;
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
        DigestionOccurrenceAnswerGetResponse digestionOccurrenceAnswerGetResponse = (DigestionOccurrenceAnswerGetResponse) o;
        return Objects.equals(id, digestionOccurrenceAnswerGetResponse.id) &&
                Objects.equals(digestionOccurrenceId, digestionOccurrenceAnswerGetResponse.digestionOccurrenceId) &&
                Objects.equals(creationTimestamp, digestionOccurrenceAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, digestionOccurrenceAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, digestionOccurrenceId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DigestionOccurrenceAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("digestionOccurrenceId='" + digestionOccurrenceId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static DigestionOccurrenceAnswerGetResponse from(DigestionOccurrenceAnswer that) {
        return new DigestionOccurrenceAnswerGetResponse(that.getId(), that.getDigestionOccurrenceId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
