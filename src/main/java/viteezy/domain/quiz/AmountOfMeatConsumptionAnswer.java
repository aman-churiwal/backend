package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AmountOfMeatConsumptionAnswer {

    private final Long id;
    private final Long quizId;
    private final Long amountOfMeatConsumptionId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public AmountOfMeatConsumptionAnswer(
            Long id, Long quizId, Long amountOfMeatConsumptionId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.amountOfMeatConsumptionId = amountOfMeatConsumptionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
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
        AmountOfMeatConsumptionAnswer amountOfMeatConsumption = (AmountOfMeatConsumptionAnswer) o;
        return Objects.equals(id, amountOfMeatConsumption.id) &&
                Objects.equals(quizId, amountOfMeatConsumption.quizId) &&
                Objects.equals(amountOfMeatConsumptionId, amountOfMeatConsumption.amountOfMeatConsumptionId) &&
                Objects.equals(creationTimestamp, amountOfMeatConsumption.creationTimestamp) &&
                Objects.equals(modificationTimestamp, amountOfMeatConsumption.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, amountOfMeatConsumptionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("amountOfMeatConsumptionId='" + amountOfMeatConsumptionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
