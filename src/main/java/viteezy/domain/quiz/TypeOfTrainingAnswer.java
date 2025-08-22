package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class TypeOfTrainingAnswer {

    private final Long id;
    private final Long quizId;
    private final Long typeOfTrainingId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public TypeOfTrainingAnswer(
            Long id, Long quizId, Long typeOfTrainingId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.typeOfTrainingId = typeOfTrainingId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getTypeOfTrainingId() {
        return typeOfTrainingId;
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
        TypeOfTrainingAnswer typeOfTraining = (TypeOfTrainingAnswer) o;
        return Objects.equals(id, typeOfTraining.id) &&
                Objects.equals(quizId, typeOfTraining.quizId) &&
                Objects.equals(typeOfTrainingId, typeOfTraining.typeOfTrainingId) &&
                Objects.equals(creationTimestamp, typeOfTraining.creationTimestamp) &&
                Objects.equals(modificationTimestamp, typeOfTraining.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, typeOfTrainingId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("typeOfTrainingId='" + typeOfTrainingId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
