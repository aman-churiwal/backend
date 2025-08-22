package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AverageSleepingHoursAnswer {

    private final Long id;
    private final Long quizId;
    private final Long averageSleepingHoursId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public AverageSleepingHoursAnswer(
            Long id, Long quizId, Long averageSleepingHoursId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.averageSleepingHoursId = averageSleepingHoursId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getAverageSleepingHoursId() {
        return averageSleepingHoursId;
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
        AverageSleepingHoursAnswer averageSleepingHours = (AverageSleepingHoursAnswer) o;
        return Objects.equals(id, averageSleepingHours.id) &&
                Objects.equals(quizId, averageSleepingHours.quizId) &&
                Objects.equals(averageSleepingHoursId, averageSleepingHours.averageSleepingHoursId) &&
                Objects.equals(creationTimestamp, averageSleepingHours.creationTimestamp) &&
                Objects.equals(modificationTimestamp, averageSleepingHours.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, averageSleepingHoursId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("averageSleepingHoursId='" + averageSleepingHoursId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
