package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.AmountOfVegetableConsumptionAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AmountOfVegetableConsumptionAnswerGetResponse {

    private final Long id;
    private final Long amountOfVegetableConsumptionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public AmountOfVegetableConsumptionAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "amountOfVegetableConsumptionId", required = true) Long amountOfVegetableConsumptionId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.amountOfVegetableConsumptionId = amountOfVegetableConsumptionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getAmountOfVegetableConsumptionId() {
        return amountOfVegetableConsumptionId;
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
        AmountOfVegetableConsumptionAnswerGetResponse amountOfVegetableConsumptionAnswerGetResponse = (AmountOfVegetableConsumptionAnswerGetResponse) o;
        return Objects.equals(id, amountOfVegetableConsumptionAnswerGetResponse.id) &&
                Objects.equals(amountOfVegetableConsumptionId, amountOfVegetableConsumptionAnswerGetResponse.amountOfVegetableConsumptionId) &&
                Objects.equals(creationTimestamp, amountOfVegetableConsumptionAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, amountOfVegetableConsumptionAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amountOfVegetableConsumptionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AmountOfVegetableConsumptionAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("amountOfVegetableConsumptionId='" + amountOfVegetableConsumptionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static AmountOfVegetableConsumptionAnswerGetResponse from(AmountOfVegetableConsumptionAnswer that) {
        return new AmountOfVegetableConsumptionAnswerGetResponse(that.getId(), that.getAmountOfVegetableConsumptionId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
