package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.EasternMedicineOpinionAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class EasternMedicineOpinionAnswerGetResponse {

    private final Long id;
    private final Long easternMedicineOpinionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public EasternMedicineOpinionAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "easternMedicineOpinionId", required = true) Long easternMedicineOpinionId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.easternMedicineOpinionId = easternMedicineOpinionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getEasternMedicineOpinionId() {
        return easternMedicineOpinionId;
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
        EasternMedicineOpinionAnswerGetResponse easternMedicineOpinionAnswerGetResponse = (EasternMedicineOpinionAnswerGetResponse) o;
        return Objects.equals(id, easternMedicineOpinionAnswerGetResponse.id) &&
                Objects.equals(easternMedicineOpinionId, easternMedicineOpinionAnswerGetResponse.easternMedicineOpinionId) &&
                Objects.equals(creationTimestamp, easternMedicineOpinionAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, easternMedicineOpinionAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, easternMedicineOpinionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EasternMedicineOpinionAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("easternMedicineOpinionId='" + easternMedicineOpinionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static EasternMedicineOpinionAnswerGetResponse from(EasternMedicineOpinionAnswer that) {
        return new EasternMedicineOpinionAnswerGetResponse(that.getId(), that.getEasternMedicineOpinionId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
