package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.UrinaryInfectionAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class UrinaryInfectionAnswerGetResponse {

    private final Long id;
    private final Long urinaryInfectionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public UrinaryInfectionAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "urinaryInfectionId", required = true) Long urinaryInfectionId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.urinaryInfectionId = urinaryInfectionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getUrinaryInfectionId() {
        return urinaryInfectionId;
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
        UrinaryInfectionAnswerGetResponse urinaryInfectionAnswerGetResponse = (UrinaryInfectionAnswerGetResponse) o;
        return Objects.equals(id, urinaryInfectionAnswerGetResponse.id) &&
                Objects.equals(urinaryInfectionId, urinaryInfectionAnswerGetResponse.urinaryInfectionId) &&
                Objects.equals(creationTimestamp, urinaryInfectionAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, urinaryInfectionAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, urinaryInfectionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UrinaryInfectionAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("urinaryInfectionId='" + urinaryInfectionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static UrinaryInfectionAnswerGetResponse from(UrinaryInfectionAnswer that) {
        return new UrinaryInfectionAnswerGetResponse(that.getId(), that.getUrinaryInfectionId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
