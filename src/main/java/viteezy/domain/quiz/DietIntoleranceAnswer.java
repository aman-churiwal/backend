package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DietIntoleranceAnswer {

    private final Long id;
    private final Long quizId;
    private final Long dietIntoleranceId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public DietIntoleranceAnswer(
            Long id, Long quizId, Long dietIntoleranceId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.dietIntoleranceId = dietIntoleranceId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getDietIntoleranceId() {
        return dietIntoleranceId;
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
        DietIntoleranceAnswer dietIntolerance = (DietIntoleranceAnswer) o;
        return Objects.equals(id, dietIntolerance.id) &&
                Objects.equals(quizId, dietIntolerance.quizId) &&
                Objects.equals(dietIntoleranceId, dietIntolerance.dietIntoleranceId) &&
                Objects.equals(creationTimestamp, dietIntolerance.creationTimestamp) &&
                Objects.equals(modificationTimestamp, dietIntolerance.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, dietIntoleranceId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("dietIntoleranceId='" + dietIntoleranceId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
