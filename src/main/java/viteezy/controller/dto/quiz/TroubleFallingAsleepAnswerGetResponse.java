package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.TroubleFallingAsleepAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class TroubleFallingAsleepAnswerGetResponse {

    private final Long id;
    private final Long troubleFallingAsleepId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public TroubleFallingAsleepAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "troubleFallingAsleepId", required = true) Long troubleFallingAsleepId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.troubleFallingAsleepId = troubleFallingAsleepId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getTroubleFallingAsleepId() {
        return troubleFallingAsleepId;
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
        TroubleFallingAsleepAnswerGetResponse troubleFallingAsleepAnswerGetResponse = (TroubleFallingAsleepAnswerGetResponse) o;
        return Objects.equals(id, troubleFallingAsleepAnswerGetResponse.id) &&
                Objects.equals(troubleFallingAsleepId, troubleFallingAsleepAnswerGetResponse.troubleFallingAsleepId) &&
                Objects.equals(creationTimestamp, troubleFallingAsleepAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, troubleFallingAsleepAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, troubleFallingAsleepId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TroubleFallingAsleepAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("troubleFallingAsleepId='" + troubleFallingAsleepId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static TroubleFallingAsleepAnswerGetResponse from(TroubleFallingAsleepAnswer that) {
        return new TroubleFallingAsleepAnswerGetResponse(that.getId(), that.getTroubleFallingAsleepId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
