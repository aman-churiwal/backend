package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.AcnePlaceAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AcnePlaceAnswerGetResponse {

    private final Long id;
    private final Long acnePlaceId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public AcnePlaceAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "acnePlaceId", required = true) Long acnePlaceId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.acnePlaceId = acnePlaceId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getAcnePlaceId() {
        return acnePlaceId;
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
        AcnePlaceAnswerGetResponse acnePlaceAnswerGetResponse = (AcnePlaceAnswerGetResponse) o;
        return Objects.equals(id, acnePlaceAnswerGetResponse.id) &&
                Objects.equals(acnePlaceId, acnePlaceAnswerGetResponse.acnePlaceId) &&
                Objects.equals(creationTimestamp, acnePlaceAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, acnePlaceAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, acnePlaceId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AcnePlaceAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("acnePlaceId='" + acnePlaceId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static AcnePlaceAnswerGetResponse from(AcnePlaceAnswer that) {
        return new AcnePlaceAnswerGetResponse(that.getId(), that.getAcnePlaceId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
