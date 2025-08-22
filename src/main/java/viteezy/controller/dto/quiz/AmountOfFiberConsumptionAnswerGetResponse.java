package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.AmountOfFiberConsumptionAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AmountOfFiberConsumptionAnswerGetResponse {

    private final Long id;
    private final Long amountOfFiberConsumptionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public AmountOfFiberConsumptionAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "amountOfFiberConsumptionId", required = true) Long amountOfFiberConsumptionId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.amountOfFiberConsumptionId = amountOfFiberConsumptionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getAmountOfFiberConsumptionId() {
        return amountOfFiberConsumptionId;
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
        AmountOfFiberConsumptionAnswerGetResponse amountOfFiberConsumptionAnswerGetResponse = (AmountOfFiberConsumptionAnswerGetResponse) o;
        return Objects.equals(id, amountOfFiberConsumptionAnswerGetResponse.id) &&
                Objects.equals(amountOfFiberConsumptionId, amountOfFiberConsumptionAnswerGetResponse.amountOfFiberConsumptionId) &&
                Objects.equals(creationTimestamp, amountOfFiberConsumptionAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, amountOfFiberConsumptionAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amountOfFiberConsumptionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AmountOfFiberConsumptionAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("amountOfFiberConsumptionId='" + amountOfFiberConsumptionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static AmountOfFiberConsumptionAnswerGetResponse from(AmountOfFiberConsumptionAnswer that) {
        return new AmountOfFiberConsumptionAnswerGetResponse(that.getId(), that.getAmountOfFiberConsumptionId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
