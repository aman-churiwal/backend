package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class EasternMedicineOpinionAnswer {

    private final Long id;
    private final Long quizId;
    private final Long easternMedicineOpinionId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public EasternMedicineOpinionAnswer(
            Long id, Long quizId, Long easternMedicineOpinionId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.easternMedicineOpinionId = easternMedicineOpinionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getEasternMedicineOpinionId() {
        return easternMedicineOpinionId;
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
        EasternMedicineOpinionAnswer easternMedicineOpinion = (EasternMedicineOpinionAnswer) o;
        return Objects.equals(id, easternMedicineOpinion.id) &&
                Objects.equals(quizId, easternMedicineOpinion.quizId) &&
                Objects.equals(easternMedicineOpinionId, easternMedicineOpinion.easternMedicineOpinionId) &&
                Objects.equals(creationTimestamp, easternMedicineOpinion.creationTimestamp) &&
                Objects.equals(modificationTimestamp, easternMedicineOpinion.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, easternMedicineOpinionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("easternMedicineOpinionId='" + easternMedicineOpinionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
