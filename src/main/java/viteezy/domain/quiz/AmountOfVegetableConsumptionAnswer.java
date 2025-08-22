package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AmountOfVegetableConsumptionAnswer {

    private final Long id;
    private final Long quizId;
    private final Long amountOfVegetableConsumptionId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public AmountOfVegetableConsumptionAnswer(
            Long id, Long quizId, Long amountOfVegetableConsumptionId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.amountOfVegetableConsumptionId = amountOfVegetableConsumptionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
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
        AmountOfVegetableConsumptionAnswer amountOfVegetableConsumption = (AmountOfVegetableConsumptionAnswer) o;
        return Objects.equals(id, amountOfVegetableConsumption.id) &&
                Objects.equals(quizId, amountOfVegetableConsumption.quizId) &&
                Objects.equals(amountOfVegetableConsumptionId, amountOfVegetableConsumption.amountOfVegetableConsumptionId) &&
                Objects.equals(creationTimestamp, amountOfVegetableConsumption.creationTimestamp) &&
                Objects.equals(modificationTimestamp, amountOfVegetableConsumption.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, amountOfVegetableConsumptionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("amountOfVegetableConsumptionId='" + amountOfVegetableConsumptionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
