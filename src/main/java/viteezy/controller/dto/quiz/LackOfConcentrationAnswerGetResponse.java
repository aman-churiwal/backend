package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.LackOfConcentrationAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class LackOfConcentrationAnswerGetResponse {

    private final Long id;
    private final Long lackOfConcentrationId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public LackOfConcentrationAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "lackOfConcentrationId", required = true) Long lackOfConcentrationId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.lackOfConcentrationId = lackOfConcentrationId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getLackOfConcentrationId() {
        return lackOfConcentrationId;
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
        LackOfConcentrationAnswerGetResponse lackOfConcentrationAnswerGetResponse = (LackOfConcentrationAnswerGetResponse) o;
        return Objects.equals(id, lackOfConcentrationAnswerGetResponse.id) &&
                Objects.equals(lackOfConcentrationId, lackOfConcentrationAnswerGetResponse.lackOfConcentrationId) &&
                Objects.equals(creationTimestamp, lackOfConcentrationAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, lackOfConcentrationAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lackOfConcentrationId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LackOfConcentrationAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("lackOfConcentrationId='" + lackOfConcentrationId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static LackOfConcentrationAnswerGetResponse from(LackOfConcentrationAnswer that) {
        return new LackOfConcentrationAnswerGetResponse(that.getId(), that.getLackOfConcentrationId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
