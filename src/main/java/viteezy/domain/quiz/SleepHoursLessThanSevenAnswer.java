package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SleepHoursLessThanSevenAnswer {

    private final Long id;
    private final Long quizId;
    private final Long sleepHoursLessThanSevenId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public SleepHoursLessThanSevenAnswer(
            Long id, Long quizId, Long sleepHoursLessThanSevenId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.sleepHoursLessThanSevenId = sleepHoursLessThanSevenId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getSleepHoursLessThanSevenId() {
        return sleepHoursLessThanSevenId;
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
        SleepHoursLessThanSevenAnswer sleepHoursLessThanSeven = (SleepHoursLessThanSevenAnswer) o;
        return Objects.equals(id, sleepHoursLessThanSeven.id) &&
                Objects.equals(quizId, sleepHoursLessThanSeven.quizId) &&
                Objects.equals(sleepHoursLessThanSevenId, sleepHoursLessThanSeven.sleepHoursLessThanSevenId) &&
                Objects.equals(creationTimestamp, sleepHoursLessThanSeven.creationTimestamp) &&
                Objects.equals(modificationTimestamp, sleepHoursLessThanSeven.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, sleepHoursLessThanSevenId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("sleepHoursLessThanSevenId='" + sleepHoursLessThanSevenId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
