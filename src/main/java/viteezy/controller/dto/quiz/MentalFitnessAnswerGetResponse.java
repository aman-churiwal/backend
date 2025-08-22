package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.MentalFitnessAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class MentalFitnessAnswerGetResponse {

    private final Long id;
    private final Long mentalFitnessId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public MentalFitnessAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "mentalFitnessId", required = true) Long mentalFitnessId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.mentalFitnessId = mentalFitnessId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getMentalFitnessId() {
        return mentalFitnessId;
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
        MentalFitnessAnswerGetResponse mentalFitnessAnswerGetResponse = (MentalFitnessAnswerGetResponse) o;
        return Objects.equals(id, mentalFitnessAnswerGetResponse.id) &&
                Objects.equals(mentalFitnessId, mentalFitnessAnswerGetResponse.mentalFitnessId) &&
                Objects.equals(creationTimestamp, mentalFitnessAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, mentalFitnessAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mentalFitnessId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MentalFitnessAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("mentalFitnessId='" + mentalFitnessId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static MentalFitnessAnswerGetResponse from(MentalFitnessAnswer that) {
        return new MentalFitnessAnswerGetResponse(that.getId(), that.getMentalFitnessId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
