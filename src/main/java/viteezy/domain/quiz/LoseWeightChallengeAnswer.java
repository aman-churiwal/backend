package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class LoseWeightChallengeAnswer {

    private final Long id;
    private final Long quizId;
    private final Long loseWeightChallengeId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public LoseWeightChallengeAnswer(
            Long id, Long quizId, Long loseWeightChallengeId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.loseWeightChallengeId = loseWeightChallengeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getLoseWeightChallengeId() {
        return loseWeightChallengeId;
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
        LoseWeightChallengeAnswer loseWeightChallenge = (LoseWeightChallengeAnswer) o;
        return Objects.equals(id, loseWeightChallenge.id) &&
                Objects.equals(quizId, loseWeightChallenge.quizId) &&
                Objects.equals(loseWeightChallengeId, loseWeightChallenge.loseWeightChallengeId) &&
                Objects.equals(creationTimestamp, loseWeightChallenge.creationTimestamp) &&
                Objects.equals(modificationTimestamp, loseWeightChallenge.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, loseWeightChallengeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("loseWeightChallengeId='" + loseWeightChallengeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
