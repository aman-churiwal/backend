package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.VitaminIntakeAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class VitaminIntakeAnswerGetResponse {

    private final Long id;
    private final Long vitaminIntakeId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public VitaminIntakeAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "vitaminIntakeId", required = true) Long vitaminIntakeId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.vitaminIntakeId = vitaminIntakeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getVitaminIntakeId() {
        return vitaminIntakeId;
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
        VitaminIntakeAnswerGetResponse vitaminIntakeAnswerGetResponse = (VitaminIntakeAnswerGetResponse) o;
        return Objects.equals(id, vitaminIntakeAnswerGetResponse.id) &&
                Objects.equals(vitaminIntakeId, vitaminIntakeAnswerGetResponse.vitaminIntakeId) &&
                Objects.equals(creationTimestamp, vitaminIntakeAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, vitaminIntakeAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vitaminIntakeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VitaminIntakeAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("vitaminIntakeId='" + vitaminIntakeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static VitaminIntakeAnswerGetResponse from(VitaminIntakeAnswer that) {
        return new VitaminIntakeAnswerGetResponse(that.getId(), that.getVitaminIntakeId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
