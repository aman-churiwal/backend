package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class PregnancyStateAnswer {

    private final Long id;
    private final Long quizId;
    private final Long pregnancyStateId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public PregnancyStateAnswer(
            Long id, Long quizId, Long pregnancyStateId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.pregnancyStateId = pregnancyStateId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getPregnancyStateId() {
        return pregnancyStateId;
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
        PregnancyStateAnswer pregnancyState = (PregnancyStateAnswer) o;
        return Objects.equals(id, pregnancyState.id) &&
                Objects.equals(quizId, pregnancyState.quizId) &&
                Objects.equals(pregnancyStateId, pregnancyState.pregnancyStateId) &&
                Objects.equals(creationTimestamp, pregnancyState.creationTimestamp) &&
                Objects.equals(modificationTimestamp, pregnancyState.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, pregnancyStateId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("pregnancyStateId='" + pregnancyStateId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
