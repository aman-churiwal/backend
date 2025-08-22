package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.AmountOfFruitConsumptionAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AmountOfFruitConsumptionAnswerGetResponse {

    private final Long id;
    private final Long amountOfFruitConsumptionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public AmountOfFruitConsumptionAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "amountOfFruitConsumptionId", required = true) Long amountOfFruitConsumptionId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.amountOfFruitConsumptionId = amountOfFruitConsumptionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getAmountOfFruitConsumptionId() {
        return amountOfFruitConsumptionId;
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
        AmountOfFruitConsumptionAnswerGetResponse amountOfFruitConsumptionAnswerGetResponse = (AmountOfFruitConsumptionAnswerGetResponse) o;
        return Objects.equals(id, amountOfFruitConsumptionAnswerGetResponse.id) &&
                Objects.equals(amountOfFruitConsumptionId, amountOfFruitConsumptionAnswerGetResponse.amountOfFruitConsumptionId) &&
                Objects.equals(creationTimestamp, amountOfFruitConsumptionAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, amountOfFruitConsumptionAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amountOfFruitConsumptionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AmountOfFruitConsumptionAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("amountOfFruitConsumptionId='" + amountOfFruitConsumptionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static AmountOfFruitConsumptionAnswerGetResponse from(AmountOfFruitConsumptionAnswer that) {
        return new AmountOfFruitConsumptionAnswerGetResponse(that.getId(), that.getAmountOfFruitConsumptionId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
