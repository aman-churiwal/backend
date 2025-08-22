package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AmountOfDairyConsumptionAnswer {

    private final Long id;
    private final Long quizId;
    private final Long amountOfDairyConsumptionId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public AmountOfDairyConsumptionAnswer(
            Long id, Long quizId, Long amountOfDairyConsumptionId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.amountOfDairyConsumptionId = amountOfDairyConsumptionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
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
        AmountOfDairyConsumptionAnswer amountOfDairyConsumption = (AmountOfDairyConsumptionAnswer) o;
        return Objects.equals(id, amountOfDairyConsumption.id) &&
                Objects.equals(quizId, amountOfDairyConsumption.quizId) &&
                Objects.equals(amountOfDairyConsumptionId, amountOfDairyConsumption.amountOfDairyConsumptionId) &&
                Objects.equals(creationTimestamp, amountOfDairyConsumption.creationTimestamp) &&
                Objects.equals(modificationTimestamp, amountOfDairyConsumption.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, amountOfDairyConsumptionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("amountOfDairyConsumptionId='" + amountOfDairyConsumptionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
