package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AmountOfFiberConsumptionAnswer {

    private final Long id;
    private final Long quizId;
    private final Long amountOfFiberConsumptionId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public AmountOfFiberConsumptionAnswer(
            Long id, Long quizId, Long amountOfFiberConsumptionId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.amountOfFiberConsumptionId = amountOfFiberConsumptionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
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
        AmountOfFiberConsumptionAnswer amountOfFiberConsumption = (AmountOfFiberConsumptionAnswer) o;
        return Objects.equals(id, amountOfFiberConsumption.id) &&
                Objects.equals(quizId, amountOfFiberConsumption.quizId) &&
                Objects.equals(amountOfFiberConsumptionId, amountOfFiberConsumption.amountOfFiberConsumptionId) &&
                Objects.equals(creationTimestamp, amountOfFiberConsumption.creationTimestamp) &&
                Objects.equals(modificationTimestamp, amountOfFiberConsumption.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, amountOfFiberConsumptionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("amountOfFiberConsumptionId='" + amountOfFiberConsumptionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
