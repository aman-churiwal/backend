package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AmountOfProteinConsumptionAnswer {

    private final Long id;
    private final Long quizId;
    private final Long amountOfProteinConsumptionId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public AmountOfProteinConsumptionAnswer(
            Long id, Long quizId, Long amountOfProteinConsumptionId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.amountOfProteinConsumptionId = amountOfProteinConsumptionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getAmountOfProteinConsumptionId() {
        return amountOfProteinConsumptionId;
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
        AmountOfProteinConsumptionAnswer amountOfProteinConsumption = (AmountOfProteinConsumptionAnswer) o;
        return Objects.equals(id, amountOfProteinConsumption.id) &&
                Objects.equals(quizId, amountOfProteinConsumption.quizId) &&
                Objects.equals(amountOfProteinConsumptionId, amountOfProteinConsumption.amountOfProteinConsumptionId) &&
                Objects.equals(creationTimestamp, amountOfProteinConsumption.creationTimestamp) &&
                Objects.equals(modificationTimestamp, amountOfProteinConsumption.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, amountOfProteinConsumptionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("amountOfProteinConsumptionId='" + amountOfProteinConsumptionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
