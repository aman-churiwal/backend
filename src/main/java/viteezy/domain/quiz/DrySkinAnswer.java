package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DrySkinAnswer {

    private final Long id;
    private final Long quizId;
    private final Long drySkinId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public DrySkinAnswer(
            Long id, Long quizId, Long drySkinId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.drySkinId = drySkinId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getDrySkinId() {
        return drySkinId;
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
        DrySkinAnswer drySkin = (DrySkinAnswer) o;
        return Objects.equals(id, drySkin.id) &&
                Objects.equals(quizId, drySkin.quizId) &&
                Objects.equals(drySkinId, drySkin.drySkinId) &&
                Objects.equals(creationTimestamp, drySkin.creationTimestamp) &&
                Objects.equals(modificationTimestamp, drySkin.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, drySkinId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("drySkinId='" + drySkinId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
