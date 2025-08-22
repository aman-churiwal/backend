package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.TrainingIntensivelyAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class TrainingIntensivelyAnswerGetResponse {

    private final Long id;
    private final Long trainingIntensivelyId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public TrainingIntensivelyAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "trainingIntensivelyId", required = true) Long trainingIntensivelyId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.trainingIntensivelyId = trainingIntensivelyId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getTrainingIntensivelyId() {
        return trainingIntensivelyId;
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
        TrainingIntensivelyAnswerGetResponse trainingIntensivelyAnswerGetResponse = (TrainingIntensivelyAnswerGetResponse) o;
        return Objects.equals(id, trainingIntensivelyAnswerGetResponse.id) &&
                Objects.equals(trainingIntensivelyId, trainingIntensivelyAnswerGetResponse.trainingIntensivelyId) &&
                Objects.equals(creationTimestamp, trainingIntensivelyAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, trainingIntensivelyAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trainingIntensivelyId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TrainingIntensivelyAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("trainingIntensivelyId='" + trainingIntensivelyId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static TrainingIntensivelyAnswerGetResponse from(TrainingIntensivelyAnswer that) {
        return new TrainingIntensivelyAnswerGetResponse(that.getId(), that.getTrainingIntensivelyId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
