package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class HealthComplaintsAnswer {

    private final Long id;
    private final Long quizId;
    private final Long healthComplaintsId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public HealthComplaintsAnswer(
            Long id, Long quizId, Long healthComplaintsId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.healthComplaintsId = healthComplaintsId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getHealthComplaintsId() {
        return healthComplaintsId;
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
        HealthComplaintsAnswer healthComplaints = (HealthComplaintsAnswer) o;
        return Objects.equals(id, healthComplaints.id) &&
                Objects.equals(quizId, healthComplaints.quizId) &&
                Objects.equals(healthComplaintsId, healthComplaints.healthComplaintsId) &&
                Objects.equals(creationTimestamp, healthComplaints.creationTimestamp) &&
                Objects.equals(modificationTimestamp, healthComplaints.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, healthComplaintsId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("healthComplaintsId='" + healthComplaintsId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
