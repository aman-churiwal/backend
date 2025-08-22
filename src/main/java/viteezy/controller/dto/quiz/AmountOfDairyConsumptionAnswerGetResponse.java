package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.AmountOfDairyConsumptionAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AmountOfDairyConsumptionAnswerGetResponse {

    private final Long id;
    private final Long amountOfDairyConsumptionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public AmountOfDairyConsumptionAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "amountOfDairyConsumptionId", required = true) Long amountOfDairyConsumptionId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.amountOfDairyConsumptionId = amountOfDairyConsumptionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getAmountOfDairyConsumptionId() {
        return amountOfDairyConsumptionId;
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
        AmountOfDairyConsumptionAnswerGetResponse amountOfDairyConsumptionAnswerGetResponse = (AmountOfDairyConsumptionAnswerGetResponse) o;
        return Objects.equals(id, amountOfDairyConsumptionAnswerGetResponse.id) &&
                Objects.equals(amountOfDairyConsumptionId, amountOfDairyConsumptionAnswerGetResponse.amountOfDairyConsumptionId) &&
                Objects.equals(creationTimestamp, amountOfDairyConsumptionAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, amountOfDairyConsumptionAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amountOfDairyConsumptionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AmountOfDairyConsumptionAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("amountOfDairyConsumptionId='" + amountOfDairyConsumptionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static AmountOfDairyConsumptionAnswerGetResponse from(AmountOfDairyConsumptionAnswer that) {
        return new AmountOfDairyConsumptionAnswerGetResponse(that.getId(), that.getAmountOfDairyConsumptionId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
