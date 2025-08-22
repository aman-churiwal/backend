package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class NailImprovementAnswer {

    private final Long id;
    private final Long quizId;
    private final Long nailImprovementId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public NailImprovementAnswer(
            Long id, Long quizId, Long nailImprovementId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.nailImprovementId = nailImprovementId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getNailImprovementId() {
        return nailImprovementId;
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
        NailImprovementAnswer nailImprovement = (NailImprovementAnswer) o;
        return Objects.equals(id, nailImprovement.id) &&
                Objects.equals(quizId, nailImprovement.quizId) &&
                Objects.equals(nailImprovementId, nailImprovement.nailImprovementId) &&
                Objects.equals(creationTimestamp, nailImprovement.creationTimestamp) &&
                Objects.equals(modificationTimestamp, nailImprovement.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, nailImprovementId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("nailImprovementId='" + nailImprovementId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
