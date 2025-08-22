package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class UsageGoalAnswer {

    private final Long id;
    private final Long quizId;
    private final Long usageGoalId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public UsageGoalAnswer(
            Long id, Long quizId, Long usageGoalId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.usageGoalId = usageGoalId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getUsageGoalId() {
        return usageGoalId;
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
        UsageGoalAnswer usageGoal = (UsageGoalAnswer) o;
        return Objects.equals(id, usageGoal.id) &&
                Objects.equals(quizId, usageGoal.quizId) &&
                Objects.equals(usageGoalId, usageGoal.usageGoalId) &&
                Objects.equals(creationTimestamp, usageGoal.creationTimestamp) &&
                Objects.equals(modificationTimestamp, usageGoal.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, usageGoalId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("usageGoalId='" + usageGoalId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
