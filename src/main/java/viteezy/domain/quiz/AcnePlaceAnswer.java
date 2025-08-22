package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AcnePlaceAnswer {

    private final Long id;
    private final Long quizId;
    private final Long acnePlaceId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public AcnePlaceAnswer(
            Long id, Long quizId, Long acnePlaceId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.acnePlaceId = acnePlaceId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getAcnePlaceId() {
        return acnePlaceId;
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
        AcnePlaceAnswer acnePlace = (AcnePlaceAnswer) o;
        return Objects.equals(id, acnePlace.id) &&
                Objects.equals(quizId, acnePlace.quizId) &&
                Objects.equals(acnePlaceId, acnePlace.acnePlaceId) &&
                Objects.equals(creationTimestamp, acnePlace.creationTimestamp) &&
                Objects.equals(modificationTimestamp, acnePlace.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, acnePlaceId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("acnePlaceId='" + acnePlaceId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
