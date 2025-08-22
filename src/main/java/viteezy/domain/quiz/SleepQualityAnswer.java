package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SleepQualityAnswer {

    private final Long id;
    private final Long quizId;
    private final Long sleepQualityId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public SleepQualityAnswer(
            Long id, Long quizId, Long sleepQualityId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.sleepQualityId = sleepQualityId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getSleepQualityId() {
        return sleepQualityId;
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
        SleepQualityAnswer sleepQuality = (SleepQualityAnswer) o;
        return Objects.equals(id, sleepQuality.id) &&
                Objects.equals(quizId, sleepQuality.quizId) &&
                Objects.equals(sleepQualityId, sleepQuality.sleepQualityId) &&
                Objects.equals(creationTimestamp, sleepQuality.creationTimestamp) &&
                Objects.equals(modificationTimestamp, sleepQuality.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, sleepQualityId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("sleepQualityId='" + sleepQualityId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
