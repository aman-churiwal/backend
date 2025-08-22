package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.AmountOfProteinConsumptionAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AmountOfProteinConsumptionAnswerGetResponse {

    private final Long id;
    private final Long amountOfProteinConsumptionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public AmountOfProteinConsumptionAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "amountOfProteinConsumptionId", required = true) Long amountOfProteinConsumptionId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.amountOfProteinConsumptionId = amountOfProteinConsumptionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getAmountOfProteinConsumptionId() {
        return amountOfProteinConsumptionId;
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
        AmountOfProteinConsumptionAnswerGetResponse amountOfProteinConsumptionAnswerGetResponse = (AmountOfProteinConsumptionAnswerGetResponse) o;
        return Objects.equals(id, amountOfProteinConsumptionAnswerGetResponse.id) &&
                Objects.equals(amountOfProteinConsumptionId, amountOfProteinConsumptionAnswerGetResponse.amountOfProteinConsumptionId) &&
                Objects.equals(creationTimestamp, amountOfProteinConsumptionAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, amountOfProteinConsumptionAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amountOfProteinConsumptionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AmountOfProteinConsumptionAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("amountOfProteinConsumptionId='" + amountOfProteinConsumptionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static AmountOfProteinConsumptionAnswerGetResponse from(AmountOfProteinConsumptionAnswer that) {
        return new AmountOfProteinConsumptionAnswerGetResponse(that.getId(), that.getAmountOfProteinConsumptionId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
