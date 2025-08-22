package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class HelpGoalAnswer {

    private final Long id;
    private final Long quizId;
    private final Long helpGoalId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public HelpGoalAnswer(
            Long id, Long quizId, Long helpGoalId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.helpGoalId = helpGoalId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getHelpGoalId() {
        return helpGoalId;
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
        HelpGoalAnswer helpGoal = (HelpGoalAnswer) o;
        return Objects.equals(id, helpGoal.id) &&
                Objects.equals(quizId, helpGoal.quizId) &&
                Objects.equals(helpGoalId, helpGoal.helpGoalId) &&
                Objects.equals(creationTimestamp, helpGoal.creationTimestamp) &&
                Objects.equals(modificationTimestamp, helpGoal.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, helpGoalId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("helpGoalId='" + helpGoalId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
