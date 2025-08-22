package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class BingeEatingReasonAnswer {

    private final Long id;
    private final Long quizId;
    private final Long bingeEatingReasonId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public BingeEatingReasonAnswer(
            Long id, Long quizId, Long bingeEatingReasonId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.bingeEatingReasonId = bingeEatingReasonId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getBingeEatingReasonId() {
        return bingeEatingReasonId;
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
        BingeEatingReasonAnswer bingeEatingReason = (BingeEatingReasonAnswer) o;
        return Objects.equals(id, bingeEatingReason.id) &&
                Objects.equals(quizId, bingeEatingReason.quizId) &&
                Objects.equals(bingeEatingReasonId, bingeEatingReason.bingeEatingReasonId) &&
                Objects.equals(creationTimestamp, bingeEatingReason.creationTimestamp) &&
                Objects.equals(modificationTimestamp, bingeEatingReason.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, bingeEatingReasonId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("bingeEatingReasonId='" + bingeEatingReasonId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
