package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.ThirtyMinutesOfSunAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class ThirtyMinutesOfSunAnswerGetResponse {

    private final Long id;
    private final Long thirtyMinutesOfSunId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public ThirtyMinutesOfSunAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "thirtyMinutesOfSunId", required = true) Long thirtyMinutesOfSunId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.thirtyMinutesOfSunId = thirtyMinutesOfSunId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getThirtyMinutesOfSunId() {
        return thirtyMinutesOfSunId;
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
        ThirtyMinutesOfSunAnswerGetResponse thirtyMinutesOfSunAnswerGetResponse = (ThirtyMinutesOfSunAnswerGetResponse) o;
        return Objects.equals(id, thirtyMinutesOfSunAnswerGetResponse.id) &&
                Objects.equals(thirtyMinutesOfSunId, thirtyMinutesOfSunAnswerGetResponse.thirtyMinutesOfSunId) &&
                Objects.equals(creationTimestamp, thirtyMinutesOfSunAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, thirtyMinutesOfSunAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, thirtyMinutesOfSunId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ThirtyMinutesOfSunAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("thirtyMinutesOfSunId='" + thirtyMinutesOfSunId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static ThirtyMinutesOfSunAnswerGetResponse from(ThirtyMinutesOfSunAnswer that) {
        return new ThirtyMinutesOfSunAnswerGetResponse(that.getId(), that.getThirtyMinutesOfSunId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
