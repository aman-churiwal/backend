package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class PrimaryGoalAnswer {

    private final Long id;
    private final Long quizId;
    private final Long primaryGoalId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public PrimaryGoalAnswer(
            Long id, Long quizId, Long primaryGoalId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.primaryGoalId = primaryGoalId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getPrimaryGoalId() {
        return primaryGoalId;
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
        PrimaryGoalAnswer primaryGoal = (PrimaryGoalAnswer) o;
        return Objects.equals(id, primaryGoal.id) &&
                Objects.equals(quizId, primaryGoal.quizId) &&
                Objects.equals(primaryGoalId, primaryGoal.primaryGoalId) &&
                Objects.equals(creationTimestamp, primaryGoal.creationTimestamp) &&
                Objects.equals(modificationTimestamp, primaryGoal.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, primaryGoalId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("primaryGoalId='" + primaryGoalId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
