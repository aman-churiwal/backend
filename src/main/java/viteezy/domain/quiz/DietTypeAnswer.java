package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DietTypeAnswer {

    private final Long id;
    private final Long quizId;
    private final Long dietTypeId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public DietTypeAnswer(
            Long id, Long quizId, Long dietTypeId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.dietTypeId = dietTypeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getDietTypeId() {
        return dietTypeId;
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
        DietTypeAnswer dietType = (DietTypeAnswer) o;
        return Objects.equals(id, dietType.id) &&
                Objects.equals(quizId, dietType.quizId) &&
                Objects.equals(dietTypeId, dietType.dietTypeId) &&
                Objects.equals(creationTimestamp, dietType.creationTimestamp) &&
                Objects.equals(modificationTimestamp, dietType.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, dietTypeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("dietTypeId='" + dietTypeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
