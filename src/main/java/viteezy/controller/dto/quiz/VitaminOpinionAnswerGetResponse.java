package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.VitaminOpinionAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class VitaminOpinionAnswerGetResponse {

    private final Long id;
    private final Long vitaminOpinionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public VitaminOpinionAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "vitaminOpinionId", required = true) Long vitaminOpinionId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.vitaminOpinionId = vitaminOpinionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getVitaminOpinionId() {
        return vitaminOpinionId;
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
        VitaminOpinionAnswerGetResponse vitaminOpinionAnswerGetResponse = (VitaminOpinionAnswerGetResponse) o;
        return Objects.equals(id, vitaminOpinionAnswerGetResponse.id) &&
                Objects.equals(vitaminOpinionId, vitaminOpinionAnswerGetResponse.vitaminOpinionId) &&
                Objects.equals(creationTimestamp, vitaminOpinionAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, vitaminOpinionAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vitaminOpinionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VitaminOpinionAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("vitaminOpinionId='" + vitaminOpinionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static VitaminOpinionAnswerGetResponse from(VitaminOpinionAnswer that) {
        return new VitaminOpinionAnswerGetResponse(that.getId(), that.getVitaminOpinionId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
