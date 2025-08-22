package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AmountOfFruitConsumptionAnswer {

    private final Long id;
    private final Long quizId;
    private final Long amountOfFruitConsumptionId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public AmountOfFruitConsumptionAnswer(
            Long id, Long quizId, Long amountOfFruitConsumptionId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.amountOfFruitConsumptionId = amountOfFruitConsumptionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
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
        AmountOfFruitConsumptionAnswer amountOfFruitConsumption = (AmountOfFruitConsumptionAnswer) o;
        return Objects.equals(id, amountOfFruitConsumption.id) &&
                Objects.equals(quizId, amountOfFruitConsumption.quizId) &&
                Objects.equals(amountOfFruitConsumptionId, amountOfFruitConsumption.amountOfFruitConsumptionId) &&
                Objects.equals(creationTimestamp, amountOfFruitConsumption.creationTimestamp) &&
                Objects.equals(modificationTimestamp, amountOfFruitConsumption.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, amountOfFruitConsumptionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("amountOfFruitConsumptionId='" + amountOfFruitConsumptionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
