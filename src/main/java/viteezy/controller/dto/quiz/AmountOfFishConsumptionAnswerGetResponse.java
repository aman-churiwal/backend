package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.AmountOfFishConsumptionAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AmountOfFishConsumptionAnswerGetResponse {

    private final Long id;
    private final Long amountOfFishConsumptionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public AmountOfFishConsumptionAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "amountOfFishConsumptionId", required = true) Long amountOfFishConsumptionId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.amountOfFishConsumptionId = amountOfFishConsumptionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getAmountOfFishConsumptionId() {
        return amountOfFishConsumptionId;
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
        AmountOfFishConsumptionAnswerGetResponse amountOfFishConsumptionAnswerGetResponse = (AmountOfFishConsumptionAnswerGetResponse) o;
        return Objects.equals(id, amountOfFishConsumptionAnswerGetResponse.id) &&
                Objects.equals(amountOfFishConsumptionId, amountOfFishConsumptionAnswerGetResponse.amountOfFishConsumptionId) &&
                Objects.equals(creationTimestamp, amountOfFishConsumptionAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, amountOfFishConsumptionAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amountOfFishConsumptionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AmountOfFishConsumptionAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("amountOfFishConsumptionId='" + amountOfFishConsumptionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static AmountOfFishConsumptionAnswerGetResponse from(AmountOfFishConsumptionAnswer that) {
        return new AmountOfFishConsumptionAnswerGetResponse(that.getId(), that.getAmountOfFishConsumptionId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
