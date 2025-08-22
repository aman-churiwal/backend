package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SmokeAnswer {

    private final Long id;
    private final Long quizId;
    private final Long smokeId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public SmokeAnswer(
            Long id, Long quizId, Long smokeId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.smokeId = smokeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getSmokeId() {
        return smokeId;
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
        SmokeAnswer smoke = (SmokeAnswer) o;
        return Objects.equals(id, smoke.id) &&
                Objects.equals(quizId, smoke.quizId) &&
                Objects.equals(smokeId, smoke.smokeId) &&
                Objects.equals(creationTimestamp, smoke.creationTimestamp) &&
                Objects.equals(modificationTimestamp, smoke.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, smokeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("smokeId='" + smokeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
