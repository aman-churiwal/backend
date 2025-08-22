package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.DietIntoleranceAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DietIntoleranceAnswerGetResponse {

    private final Long id;
    private final Long dietIntoleranceId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public DietIntoleranceAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "dietIntoleranceId", required = true) Long dietIntoleranceId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.dietIntoleranceId = dietIntoleranceId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getDietIntoleranceId() {
        return dietIntoleranceId;
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
        DietIntoleranceAnswerGetResponse dietIntoleranceAnswerGetResponse = (DietIntoleranceAnswerGetResponse) o;
        return Objects.equals(id, dietIntoleranceAnswerGetResponse.id) &&
                Objects.equals(dietIntoleranceId, dietIntoleranceAnswerGetResponse.dietIntoleranceId) &&
                Objects.equals(creationTimestamp, dietIntoleranceAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, dietIntoleranceAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dietIntoleranceId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DietIntoleranceAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("dietIntoleranceId='" + dietIntoleranceId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static DietIntoleranceAnswerGetResponse from(DietIntoleranceAnswer that) {
        return new DietIntoleranceAnswerGetResponse(that.getId(), that.getDietIntoleranceId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
