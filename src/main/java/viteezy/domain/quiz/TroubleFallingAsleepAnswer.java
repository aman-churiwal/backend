package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class TroubleFallingAsleepAnswer {

    private final Long id;
    private final Long quizId;
    private final Long troubleFallingAsleepId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public TroubleFallingAsleepAnswer(
            Long id, Long quizId, Long troubleFallingAsleepId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.troubleFallingAsleepId = troubleFallingAsleepId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getTroubleFallingAsleepId() {
        return troubleFallingAsleepId;
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
        TroubleFallingAsleepAnswer troubleFallingAsleep = (TroubleFallingAsleepAnswer) o;
        return Objects.equals(id, troubleFallingAsleep.id) &&
                Objects.equals(quizId, troubleFallingAsleep.quizId) &&
                Objects.equals(troubleFallingAsleepId, troubleFallingAsleep.troubleFallingAsleepId) &&
                Objects.equals(creationTimestamp, troubleFallingAsleep.creationTimestamp) &&
                Objects.equals(modificationTimestamp, troubleFallingAsleep.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, troubleFallingAsleepId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("troubleFallingAsleepId='" + troubleFallingAsleepId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
