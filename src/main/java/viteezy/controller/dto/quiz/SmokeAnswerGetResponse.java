package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.SmokeAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SmokeAnswerGetResponse {

    private final Long id;
    private final Long smokeId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public SmokeAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "smokeId", required = true) Long smokeId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.smokeId = smokeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public static SmokeAnswerGetResponse from(SmokeAnswer that) {
        return new SmokeAnswerGetResponse(that.getId(), that.getSmokeId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

    public Long getId() {
        return id;
    }

    public Long getSmokeId() {
        return smokeId;
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
        SmokeAnswerGetResponse smokeAnswerGetResponse = (SmokeAnswerGetResponse) o;
        return Objects.equals(id, smokeAnswerGetResponse.id) &&
                Objects.equals(smokeId, smokeAnswerGetResponse.smokeId) &&
                Objects.equals(creationTimestamp, smokeAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, smokeAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, smokeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SmokeAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("smokeId='" + smokeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

}
