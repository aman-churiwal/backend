package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SkinProblemAnswer {

    private final Long id;
    private final Long quizId;
    private final Long skinProblemId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public SkinProblemAnswer(
            Long id, Long quizId, Long skinProblemId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.skinProblemId = skinProblemId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getSkinProblemId() {
        return skinProblemId;
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
        SkinProblemAnswer skinProblem = (SkinProblemAnswer) o;
        return Objects.equals(id, skinProblem.id) &&
                Objects.equals(quizId, skinProblem.quizId) &&
                Objects.equals(skinProblemId, skinProblem.skinProblemId) &&
                Objects.equals(creationTimestamp, skinProblem.creationTimestamp) &&
                Objects.equals(modificationTimestamp, skinProblem.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, skinProblemId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("skinProblemId='" + skinProblemId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
