package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.AllergyTypeAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AllergyTypeAnswerGetResponse {

    private final Long id;
    private final Long allergyTypeId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public AllergyTypeAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "allergyTypeId", required = true) Long allergyTypeId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.allergyTypeId = allergyTypeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getAllergyTypeId() {
        return allergyTypeId;
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
        AllergyTypeAnswerGetResponse allergyTypeAnswerGetResponse = (AllergyTypeAnswerGetResponse) o;
        return Objects.equals(id, allergyTypeAnswerGetResponse.id) &&
                Objects.equals(allergyTypeId, allergyTypeAnswerGetResponse.allergyTypeId) &&
                Objects.equals(creationTimestamp, allergyTypeAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, allergyTypeAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, allergyTypeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AllergyTypeAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("allergyTypeId='" + allergyTypeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static AllergyTypeAnswerGetResponse from(AllergyTypeAnswer that) {
        return new AllergyTypeAnswerGetResponse(that.getId(), that.getAllergyTypeId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
