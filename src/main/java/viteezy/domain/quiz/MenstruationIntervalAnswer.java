package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class MenstruationIntervalAnswer {

    private final Long id;
    private final Long quizId;
    private final Long menstruationIntervalId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public MenstruationIntervalAnswer(
            Long id, Long quizId, Long menstruationIntervalId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.menstruationIntervalId = menstruationIntervalId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getMenstruationIntervalId() {
        return menstruationIntervalId;
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
        MenstruationIntervalAnswer menstruationInterval = (MenstruationIntervalAnswer) o;
        return Objects.equals(id, menstruationInterval.id) &&
                Objects.equals(quizId, menstruationInterval.quizId) &&
                Objects.equals(menstruationIntervalId, menstruationInterval.menstruationIntervalId) &&
                Objects.equals(creationTimestamp, menstruationInterval.creationTimestamp) &&
                Objects.equals(modificationTimestamp, menstruationInterval.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, menstruationIntervalId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("menstruationIntervalId='" + menstruationIntervalId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
