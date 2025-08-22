package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SportReasonAnswer {

    private final Long id;
    private final Long quizId;
    private final Long sportReasonId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public SportReasonAnswer(
            Long id, Long quizId, Long sportReasonId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.sportReasonId = sportReasonId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getSportReasonId() {
        return sportReasonId;
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
        SportReasonAnswer sportReason = (SportReasonAnswer) o;
        return Objects.equals(id, sportReason.id) &&
                Objects.equals(quizId, sportReason.quizId) &&
                Objects.equals(sportReasonId, sportReason.sportReasonId) &&
                Objects.equals(creationTimestamp, sportReason.creationTimestamp) &&
                Objects.equals(modificationTimestamp, sportReason.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, sportReasonId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("sportReasonId='" + sportReasonId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
