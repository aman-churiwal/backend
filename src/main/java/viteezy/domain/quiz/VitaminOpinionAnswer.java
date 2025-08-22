package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class VitaminOpinionAnswer {

    private final Long id;
    private final Long quizId;
    private final Long vitaminOpinionId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public VitaminOpinionAnswer(
            Long id, Long quizId, Long vitaminOpinionId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.vitaminOpinionId = vitaminOpinionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getVitaminOpinionId() {
        return vitaminOpinionId;
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
        VitaminOpinionAnswer vitaminOpinion = (VitaminOpinionAnswer) o;
        return Objects.equals(id, vitaminOpinion.id) &&
                Objects.equals(quizId, vitaminOpinion.quizId) &&
                Objects.equals(vitaminOpinionId, vitaminOpinion.vitaminOpinionId) &&
                Objects.equals(creationTimestamp, vitaminOpinion.creationTimestamp) &&
                Objects.equals(modificationTimestamp, vitaminOpinion.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, vitaminOpinionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("vitaminOpinionId='" + vitaminOpinionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
