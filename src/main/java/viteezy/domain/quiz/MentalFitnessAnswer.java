package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class MentalFitnessAnswer {

    private final Long id;
    private final Long quizId;
    private final Long mentalFitnessId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public MentalFitnessAnswer(
            Long id, Long quizId, Long mentalFitnessId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.mentalFitnessId = mentalFitnessId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getMentalFitnessId() {
        return mentalFitnessId;
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
        MentalFitnessAnswer mentalFitness = (MentalFitnessAnswer) o;
        return Objects.equals(id, mentalFitness.id) &&
                Objects.equals(quizId, mentalFitness.quizId) &&
                Objects.equals(mentalFitnessId, mentalFitness.mentalFitnessId) &&
                Objects.equals(creationTimestamp, mentalFitness.creationTimestamp) &&
                Objects.equals(modificationTimestamp, mentalFitness.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, mentalFitnessId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("mentalFitnessId='" + mentalFitnessId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
