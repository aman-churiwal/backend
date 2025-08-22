package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class StressLevelConditionAnswer {

    private final Long id;
    private final Long quizId;
    private final Long stressLevelConditionId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public StressLevelConditionAnswer(
            Long id, Long quizId, Long stressLevelConditionId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.stressLevelConditionId = stressLevelConditionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getStressLevelConditionId() {
        return stressLevelConditionId;
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
        StressLevelConditionAnswer stressLevelCondition = (StressLevelConditionAnswer) o;
        return Objects.equals(id, stressLevelCondition.id) &&
                Objects.equals(quizId, stressLevelCondition.quizId) &&
                Objects.equals(stressLevelConditionId, stressLevelCondition.stressLevelConditionId) &&
                Objects.equals(creationTimestamp, stressLevelCondition.creationTimestamp) &&
                Objects.equals(modificationTimestamp, stressLevelCondition.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, stressLevelConditionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("stressLevelConditionId='" + stressLevelConditionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
