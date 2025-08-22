package viteezy.domain.quiz;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DateOfBirthAnswer {

    private final Long id;
    private final Long quizId;
    private final LocalDate date;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public DateOfBirthAnswer(
            Long id, Long quizId, LocalDate date, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.date = date;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public LocalDate getDate() {
        return date;
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
        DateOfBirthAnswer dateOfBirth = (DateOfBirthAnswer) o;
        return Objects.equals(id, dateOfBirth.id) &&
                Objects.equals(quizId, dateOfBirth.quizId) &&
                Objects.equals(date, dateOfBirth.date) &&
                Objects.equals(creationTimestamp, dateOfBirth.creationTimestamp) &&
                Objects.equals(modificationTimestamp, dateOfBirth.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, date, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("dateOfBirthId='" + date + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
