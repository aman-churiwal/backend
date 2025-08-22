package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class MenstruationMoodAnswer {

    private final Long id;
    private final Long quizId;
    private final Long menstruationMoodId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public MenstruationMoodAnswer(
            Long id, Long quizId, Long menstruationMoodId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.menstruationMoodId = menstruationMoodId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getMenstruationMoodId() {
        return menstruationMoodId;
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
        MenstruationMoodAnswer menstruationMood = (MenstruationMoodAnswer) o;
        return Objects.equals(id, menstruationMood.id) &&
                Objects.equals(quizId, menstruationMood.quizId) &&
                Objects.equals(menstruationMoodId, menstruationMood.menstruationMoodId) &&
                Objects.equals(creationTimestamp, menstruationMood.creationTimestamp) &&
                Objects.equals(modificationTimestamp, menstruationMood.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, menstruationMoodId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("menstruationMoodId='" + menstruationMoodId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
