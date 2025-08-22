package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class HealthyLifestyleAnswer {

    private final Long id;
    private final Long quizId;
    private final Long healthyLifestyleId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public HealthyLifestyleAnswer(
            Long id, Long quizId, Long healthyLifestyleId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.healthyLifestyleId = healthyLifestyleId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getHealthyLifestyleId() {
        return healthyLifestyleId;
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
        HealthyLifestyleAnswer healthyLifestyle = (HealthyLifestyleAnswer) o;
        return Objects.equals(id, healthyLifestyle.id) &&
                Objects.equals(quizId, healthyLifestyle.quizId) &&
                Objects.equals(healthyLifestyleId, healthyLifestyle.healthyLifestyleId) &&
                Objects.equals(creationTimestamp, healthyLifestyle.creationTimestamp) &&
                Objects.equals(modificationTimestamp, healthyLifestyle.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, healthyLifestyleId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("healthyLifestyleId='" + healthyLifestyleId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
