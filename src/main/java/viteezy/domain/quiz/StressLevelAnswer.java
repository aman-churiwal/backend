package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class StressLevelAnswer {

    private final Long id;
    private final Long quizId;
    private final Long stressLevelId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public StressLevelAnswer(
            Long id, Long quizId, Long stressLevelId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.stressLevelId = stressLevelId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getStressLevelId() {
        return stressLevelId;
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
        StressLevelAnswer stressLevel = (StressLevelAnswer) o;
        return Objects.equals(id, stressLevel.id) &&
                Objects.equals(quizId, stressLevel.quizId) &&
                Objects.equals(stressLevelId, stressLevel.stressLevelId) &&
                Objects.equals(creationTimestamp, stressLevel.creationTimestamp) &&
                Objects.equals(modificationTimestamp, stressLevel.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, stressLevelId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("stressLevelId='" + stressLevelId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
