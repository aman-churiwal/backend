package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class HairTypeAnswer {

    private final Long id;
    private final Long quizId;
    private final Long hairTypeId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public HairTypeAnswer(
            Long id, Long quizId, Long hairTypeId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.hairTypeId = hairTypeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getHairTypeId() {
        return hairTypeId;
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
        HairTypeAnswer hairType = (HairTypeAnswer) o;
        return Objects.equals(id, hairType.id) &&
                Objects.equals(quizId, hairType.quizId) &&
                Objects.equals(hairTypeId, hairType.hairTypeId) &&
                Objects.equals(creationTimestamp, hairType.creationTimestamp) &&
                Objects.equals(modificationTimestamp, hairType.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, hairTypeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("hairTypeId='" + hairTypeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
