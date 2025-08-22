package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class BirthHealthAnswer {

    private final Long id;
    private final Long quizId;
    private final Long birthHealthId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public BirthHealthAnswer(
            Long id, Long quizId, Long birthHealthId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.birthHealthId = birthHealthId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getBirthHealthId() {
        return birthHealthId;
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
        BirthHealthAnswer birthHealth = (BirthHealthAnswer) o;
        return Objects.equals(id, birthHealth.id) &&
                Objects.equals(quizId, birthHealth.quizId) &&
                Objects.equals(birthHealthId, birthHealth.birthHealthId) &&
                Objects.equals(creationTimestamp, birthHealth.creationTimestamp) &&
                Objects.equals(modificationTimestamp, birthHealth.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, birthHealthId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("birthHealthId='" + birthHealthId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
