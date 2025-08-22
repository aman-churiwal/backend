package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DigestionAmountAnswer {

    private final Long id;
    private final Long quizId;
    private final Long digestionAmountId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public DigestionAmountAnswer(
            Long id, Long quizId, Long digestionAmountId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.digestionAmountId = digestionAmountId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getDigestionAmountId() {
        return digestionAmountId;
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
        DigestionAmountAnswer digestionAmount = (DigestionAmountAnswer) o;
        return Objects.equals(id, digestionAmount.id) &&
                Objects.equals(quizId, digestionAmount.quizId) &&
                Objects.equals(digestionAmountId, digestionAmount.digestionAmountId) &&
                Objects.equals(creationTimestamp, digestionAmount.creationTimestamp) &&
                Objects.equals(modificationTimestamp, digestionAmount.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, digestionAmountId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("digestionAmountId='" + digestionAmountId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
