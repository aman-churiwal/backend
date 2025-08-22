package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class IronPrescribedAnswer {

    private final Long id;
    private final Long quizId;
    private final Long ironPrescribedId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public IronPrescribedAnswer(
            Long id, Long quizId, Long ironPrescribedId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.ironPrescribedId = ironPrescribedId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getIronPrescribedId() {
        return ironPrescribedId;
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
        IronPrescribedAnswer ironPrescribed = (IronPrescribedAnswer) o;
        return Objects.equals(id, ironPrescribed.id) &&
                Objects.equals(quizId, ironPrescribed.quizId) &&
                Objects.equals(ironPrescribedId, ironPrescribed.ironPrescribedId) &&
                Objects.equals(creationTimestamp, ironPrescribed.creationTimestamp) &&
                Objects.equals(modificationTimestamp, ironPrescribed.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, ironPrescribedId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("ironPrescribedId='" + ironPrescribedId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
