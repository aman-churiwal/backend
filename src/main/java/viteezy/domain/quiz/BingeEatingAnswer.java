package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class BingeEatingAnswer {

    private final Long id;
    private final Long quizId;
    private final Long bingeEatingId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public BingeEatingAnswer(
            Long id, Long quizId, Long bingeEatingId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.bingeEatingId = bingeEatingId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getBingeEatingId() {
        return bingeEatingId;
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
        BingeEatingAnswer bingeEating = (BingeEatingAnswer) o;
        return Objects.equals(id, bingeEating.id) &&
                Objects.equals(quizId, bingeEating.quizId) &&
                Objects.equals(bingeEatingId, bingeEating.bingeEatingId) &&
                Objects.equals(creationTimestamp, bingeEating.creationTimestamp) &&
                Objects.equals(modificationTimestamp, bingeEating.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, bingeEatingId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("bingeEatingId='" + bingeEatingId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
