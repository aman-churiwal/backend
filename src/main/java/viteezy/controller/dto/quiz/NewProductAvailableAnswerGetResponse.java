package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.NewProductAvailableAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class NewProductAvailableAnswerGetResponse {

    private final Long id;
    private final Long newProductAvailableId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public NewProductAvailableAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "newProductAvailableId", required = true) Long newProductAvailableId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.newProductAvailableId = newProductAvailableId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getNewProductAvailableId() {
        return newProductAvailableId;
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
        NewProductAvailableAnswerGetResponse newProductAvailableAnswerGetResponse = (NewProductAvailableAnswerGetResponse) o;
        return Objects.equals(id, newProductAvailableAnswerGetResponse.id) &&
                Objects.equals(newProductAvailableId, newProductAvailableAnswerGetResponse.newProductAvailableId) &&
                Objects.equals(creationTimestamp, newProductAvailableAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, newProductAvailableAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, newProductAvailableId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NewProductAvailableAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("newProductAvailableId='" + newProductAvailableId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static NewProductAvailableAnswerGetResponse from(NewProductAvailableAnswer that) {
        return new NewProductAvailableAnswerGetResponse(that.getId(), that.getNewProductAvailableId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
