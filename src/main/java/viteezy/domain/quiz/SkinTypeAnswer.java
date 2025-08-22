package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SkinTypeAnswer {

    private final Long id;
    private final Long quizId;
    private final Long skinTypeId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public SkinTypeAnswer(
            Long id, Long quizId, Long skinTypeId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.skinTypeId = skinTypeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getSkinTypeId() {
        return skinTypeId;
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
        SkinTypeAnswer skinType = (SkinTypeAnswer) o;
        return Objects.equals(id, skinType.id) &&
                Objects.equals(quizId, skinType.quizId) &&
                Objects.equals(skinTypeId, skinType.skinTypeId) &&
                Objects.equals(creationTimestamp, skinType.creationTimestamp) &&
                Objects.equals(modificationTimestamp, skinType.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, skinTypeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("skinTypeId='" + skinTypeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
