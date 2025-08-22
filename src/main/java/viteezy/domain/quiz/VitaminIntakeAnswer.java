package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class VitaminIntakeAnswer {

    private final Long id;
    private final Long quizId;
    private final Long vitaminIntakeId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public VitaminIntakeAnswer(
            Long id, Long quizId, Long vitaminIntakeId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.vitaminIntakeId = vitaminIntakeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getVitaminIntakeId() {
        return vitaminIntakeId;
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
        VitaminIntakeAnswer vitaminIntake = (VitaminIntakeAnswer) o;
        return Objects.equals(id, vitaminIntake.id) &&
                Objects.equals(quizId, vitaminIntake.quizId) &&
                Objects.equals(vitaminIntakeId, vitaminIntake.vitaminIntakeId) &&
                Objects.equals(creationTimestamp, vitaminIntake.creationTimestamp) &&
                Objects.equals(modificationTimestamp, vitaminIntake.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, vitaminIntakeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("vitaminIntakeId='" + vitaminIntakeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
