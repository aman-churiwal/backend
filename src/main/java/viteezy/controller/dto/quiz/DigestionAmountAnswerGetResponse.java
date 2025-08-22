package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.DigestionAmountAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DigestionAmountAnswerGetResponse {

    private final Long id;
    private final Long digestionAmountId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public DigestionAmountAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "digestionAmountId", required = true) Long digestionAmountId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.digestionAmountId = digestionAmountId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getDigestionAmountId() {
        return digestionAmountId;
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
        DigestionAmountAnswerGetResponse digestionAmountAnswerGetResponse = (DigestionAmountAnswerGetResponse) o;
        return Objects.equals(id, digestionAmountAnswerGetResponse.id) &&
                Objects.equals(digestionAmountId, digestionAmountAnswerGetResponse.digestionAmountId) &&
                Objects.equals(creationTimestamp, digestionAmountAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, digestionAmountAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, digestionAmountId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DigestionAmountAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("digestionAmountId='" + digestionAmountId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static DigestionAmountAnswerGetResponse from(DigestionAmountAnswer that) {
        return new DigestionAmountAnswerGetResponse(that.getId(), that.getDigestionAmountId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
