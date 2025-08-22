package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.AmountOfMeatConsumptionAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AmountOfMeatConsumptionAnswerGetResponse {

    private final Long id;
    private final Long amountOfMeatConsumptionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public AmountOfMeatConsumptionAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "amountOfMeatConsumptionId", required = true) Long amountOfMeatConsumptionId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.amountOfMeatConsumptionId = amountOfMeatConsumptionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getAmountOfMeatConsumptionId() {
        return amountOfMeatConsumptionId;
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
        AmountOfMeatConsumptionAnswerGetResponse amountOfMeatConsumptionAnswerGetResponse = (AmountOfMeatConsumptionAnswerGetResponse) o;
        return Objects.equals(id, amountOfMeatConsumptionAnswerGetResponse.id) &&
                Objects.equals(amountOfMeatConsumptionId, amountOfMeatConsumptionAnswerGetResponse.amountOfMeatConsumptionId) &&
                Objects.equals(creationTimestamp, amountOfMeatConsumptionAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, amountOfMeatConsumptionAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amountOfMeatConsumptionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AmountOfMeatConsumptionAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("amountOfMeatConsumptionId='" + amountOfMeatConsumptionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static AmountOfMeatConsumptionAnswerGetResponse from(AmountOfMeatConsumptionAnswer that) {
        return new AmountOfMeatConsumptionAnswerGetResponse(that.getId(), that.getAmountOfMeatConsumptionId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
