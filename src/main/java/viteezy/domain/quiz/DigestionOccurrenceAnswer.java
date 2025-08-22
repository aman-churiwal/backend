package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DigestionOccurrenceAnswer {

    private final Long id;
    private final Long quizId;
    private final Long digestionOccurrenceId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public DigestionOccurrenceAnswer(
            Long id, Long quizId, Long digestionOccurrenceId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.digestionOccurrenceId = digestionOccurrenceId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getDigestionOccurrenceId() {
        return digestionOccurrenceId;
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
        DigestionOccurrenceAnswer digestionOccurrence = (DigestionOccurrenceAnswer) o;
        return Objects.equals(id, digestionOccurrence.id) &&
                Objects.equals(quizId, digestionOccurrence.quizId) &&
                Objects.equals(digestionOccurrenceId, digestionOccurrence.digestionOccurrenceId) &&
                Objects.equals(creationTimestamp, digestionOccurrence.creationTimestamp) &&
                Objects.equals(modificationTimestamp, digestionOccurrence.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, digestionOccurrenceId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("digestionOccurrenceId='" + digestionOccurrenceId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
