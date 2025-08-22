package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AmountOfFishConsumptionAnswer {

    private final Long id;
    private final Long quizId;
    private final Long amountOfFishConsumptionId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public AmountOfFishConsumptionAnswer(
            Long id, Long quizId, Long amountOfFishConsumptionId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.amountOfFishConsumptionId = amountOfFishConsumptionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
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
        AmountOfFishConsumptionAnswer amountOfFishConsumption = (AmountOfFishConsumptionAnswer) o;
        return Objects.equals(id, amountOfFishConsumption.id) &&
                Objects.equals(quizId, amountOfFishConsumption.quizId) &&
                Objects.equals(amountOfFishConsumptionId, amountOfFishConsumption.amountOfFishConsumptionId) &&
                Objects.equals(creationTimestamp, amountOfFishConsumption.creationTimestamp) &&
                Objects.equals(modificationTimestamp, amountOfFishConsumption.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, amountOfFishConsumptionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("amountOfFishConsumptionId='" + amountOfFishConsumptionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
