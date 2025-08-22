package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.EnergyStateAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class EnergyStateAnswerGetResponse {

    private final Long id;
    private final Long energyStateId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public EnergyStateAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "energyStateId", required = true) Long energyStateId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.energyStateId = energyStateId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getEnergyStateId() {
        return energyStateId;
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
        EnergyStateAnswerGetResponse energyStateAnswerGetResponse = (EnergyStateAnswerGetResponse) o;
        return Objects.equals(id, energyStateAnswerGetResponse.id) &&
                Objects.equals(energyStateId, energyStateAnswerGetResponse.energyStateId) &&
                Objects.equals(creationTimestamp, energyStateAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, energyStateAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, energyStateId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EnergyStateAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("energyStateId='" + energyStateId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static EnergyStateAnswerGetResponse from(EnergyStateAnswer that) {
        return new EnergyStateAnswerGetResponse(that.getId(), that.getEnergyStateId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
