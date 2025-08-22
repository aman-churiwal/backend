package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AllergyTypeAnswer {

    private final Long id;
    private final Long quizId;
    private final Long allergyTypeId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public AllergyTypeAnswer(
            Long id, Long quizId, Long allergyTypeId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.allergyTypeId = allergyTypeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getAllergyTypeId() {
        return allergyTypeId;
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
        AllergyTypeAnswer allergyType = (AllergyTypeAnswer) o;
        return Objects.equals(id, allergyType.id) &&
                Objects.equals(quizId, allergyType.quizId) &&
                Objects.equals(allergyTypeId, allergyType.allergyTypeId) &&
                Objects.equals(creationTimestamp, allergyType.creationTimestamp) &&
                Objects.equals(modificationTimestamp, allergyType.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, allergyTypeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("allergyTypeId='" + allergyTypeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
