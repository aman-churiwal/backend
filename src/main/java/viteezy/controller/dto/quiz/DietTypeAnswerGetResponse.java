package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.DietTypeAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DietTypeAnswerGetResponse {

    private final Long id;
    private final Long dietTypeId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public DietTypeAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "dietTypeId", required = true) Long dietTypeId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.dietTypeId = dietTypeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getDietTypeId() {
        return dietTypeId;
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
        DietTypeAnswerGetResponse dietTypeAnswerGetResponse = (DietTypeAnswerGetResponse) o;
        return Objects.equals(id, dietTypeAnswerGetResponse.id) &&
                Objects.equals(dietTypeId, dietTypeAnswerGetResponse.dietTypeId) &&
                Objects.equals(creationTimestamp, dietTypeAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, dietTypeAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dietTypeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DietTypeAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("dietTypeId='" + dietTypeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static DietTypeAnswerGetResponse from(DietTypeAnswer that) {
        return new DietTypeAnswerGetResponse(that.getId(), that.getDietTypeId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
