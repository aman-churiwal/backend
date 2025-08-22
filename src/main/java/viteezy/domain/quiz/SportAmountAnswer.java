package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SportAmountAnswer {

    private final Long id;
    private final Long quizId;
    private final Long sportAmountId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public SportAmountAnswer(
            Long id, Long quizId, Long sportAmountId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.sportAmountId = sportAmountId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getSportAmountId() {
        return sportAmountId;
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
        SportAmountAnswer sportAmount = (SportAmountAnswer) o;
        return Objects.equals(id, sportAmount.id) &&
                Objects.equals(quizId, sportAmount.quizId) &&
                Objects.equals(sportAmountId, sportAmount.sportAmountId) &&
                Objects.equals(creationTimestamp, sportAmount.creationTimestamp) &&
                Objects.equals(modificationTimestamp, sportAmount.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, sportAmountId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("sportAmountId='" + sportAmountId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
