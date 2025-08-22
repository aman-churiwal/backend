package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.TypeOfTrainingAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class TypeOfTrainingAnswerGetResponse {

    private final Long id;
    private final Long typeOfTrainingId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public TypeOfTrainingAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "typeOfTrainingId", required = true) Long typeOfTrainingId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.typeOfTrainingId = typeOfTrainingId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getTypeOfTrainingId() {
        return typeOfTrainingId;
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
        TypeOfTrainingAnswerGetResponse typeOfTrainingAnswerGetResponse = (TypeOfTrainingAnswerGetResponse) o;
        return Objects.equals(id, typeOfTrainingAnswerGetResponse.id) &&
                Objects.equals(typeOfTrainingId, typeOfTrainingAnswerGetResponse.typeOfTrainingId) &&
                Objects.equals(creationTimestamp, typeOfTrainingAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, typeOfTrainingAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeOfTrainingId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TypeOfTrainingAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("typeOfTrainingId='" + typeOfTrainingId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static TypeOfTrainingAnswerGetResponse from(TypeOfTrainingAnswer that) {
        return new TypeOfTrainingAnswerGetResponse(that.getId(), that.getTypeOfTrainingId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
