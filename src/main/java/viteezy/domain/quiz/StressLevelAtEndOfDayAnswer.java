package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class StressLevelAtEndOfDayAnswer {

    private final Long id;
    private final Long quizId;
    private final Long stressLevelAtEndOfDayId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public StressLevelAtEndOfDayAnswer(
            Long id, Long quizId, Long stressLevelAtEndOfDayId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.stressLevelAtEndOfDayId = stressLevelAtEndOfDayId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getStressLevelAtEndOfDayId() {
        return stressLevelAtEndOfDayId;
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
        StressLevelAtEndOfDayAnswer stressLevelAtEndOfDay = (StressLevelAtEndOfDayAnswer) o;
        return Objects.equals(id, stressLevelAtEndOfDay.id) &&
                Objects.equals(quizId, stressLevelAtEndOfDay.quizId) &&
                Objects.equals(stressLevelAtEndOfDayId, stressLevelAtEndOfDay.stressLevelAtEndOfDayId) &&
                Objects.equals(creationTimestamp, stressLevelAtEndOfDay.creationTimestamp) &&
                Objects.equals(modificationTimestamp, stressLevelAtEndOfDay.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, stressLevelAtEndOfDayId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("stressLevelAtEndOfDayId='" + stressLevelAtEndOfDayId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
