package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class TrainingIntensivelyAnswer {

    private final Long id;
    private final Long quizId;
    private final Long trainingIntensivelyId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public TrainingIntensivelyAnswer(
            Long id, Long quizId, Long trainingIntensivelyId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.trainingIntensivelyId = trainingIntensivelyId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getTrainingIntensivelyId() {
        return trainingIntensivelyId;
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
        TrainingIntensivelyAnswer trainingIntensively = (TrainingIntensivelyAnswer) o;
        return Objects.equals(id, trainingIntensively.id) &&
                Objects.equals(quizId, trainingIntensively.quizId) &&
                Objects.equals(trainingIntensivelyId, trainingIntensively.trainingIntensivelyId) &&
                Objects.equals(creationTimestamp, trainingIntensively.creationTimestamp) &&
                Objects.equals(modificationTimestamp, trainingIntensively.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, trainingIntensivelyId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("trainingIntensivelyId='" + trainingIntensivelyId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
