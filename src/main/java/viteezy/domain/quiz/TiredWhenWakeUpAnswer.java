package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class TiredWhenWakeUpAnswer {

    private final Long id;
    private final Long quizId;
    private final Long tiredWhenWakeUpId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public TiredWhenWakeUpAnswer(
            Long id, Long quizId, Long tiredWhenWakeUpId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.tiredWhenWakeUpId = tiredWhenWakeUpId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getTiredWhenWakeUpId() {
        return tiredWhenWakeUpId;
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
        TiredWhenWakeUpAnswer tiredWhenWakeUp = (TiredWhenWakeUpAnswer) o;
        return Objects.equals(id, tiredWhenWakeUp.id) &&
                Objects.equals(quizId, tiredWhenWakeUp.quizId) &&
                Objects.equals(tiredWhenWakeUpId, tiredWhenWakeUp.tiredWhenWakeUpId) &&
                Objects.equals(creationTimestamp, tiredWhenWakeUp.creationTimestamp) &&
                Objects.equals(modificationTimestamp, tiredWhenWakeUp.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, tiredWhenWakeUpId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("tiredWhenWakeUpId='" + tiredWhenWakeUpId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
