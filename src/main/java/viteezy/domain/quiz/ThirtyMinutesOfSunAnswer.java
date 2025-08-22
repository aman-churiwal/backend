package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class ThirtyMinutesOfSunAnswer {

    private final Long id;
    private final Long quizId;
    private final Long thirtyMinutesOfSunId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public ThirtyMinutesOfSunAnswer(
            Long id, Long quizId, Long thirtyMinutesOfSunId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.thirtyMinutesOfSunId = thirtyMinutesOfSunId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getThirtyMinutesOfSunId() {
        return thirtyMinutesOfSunId;
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
        ThirtyMinutesOfSunAnswer thirtyMinutesOfSun = (ThirtyMinutesOfSunAnswer) o;
        return Objects.equals(id, thirtyMinutesOfSun.id) &&
                Objects.equals(quizId, thirtyMinutesOfSun.quizId) &&
                Objects.equals(thirtyMinutesOfSunId, thirtyMinutesOfSun.thirtyMinutesOfSunId) &&
                Objects.equals(creationTimestamp, thirtyMinutesOfSun.creationTimestamp) &&
                Objects.equals(modificationTimestamp, thirtyMinutesOfSun.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, thirtyMinutesOfSunId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("thirtyMinutesOfSunId='" + thirtyMinutesOfSunId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
