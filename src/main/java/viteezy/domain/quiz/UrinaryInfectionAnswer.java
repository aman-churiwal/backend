package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class UrinaryInfectionAnswer {

    private final Long id;
    private final Long quizId;
    private final Long urinaryInfectionId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public UrinaryInfectionAnswer(
            Long id, Long quizId, Long urinaryInfectionId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.urinaryInfectionId = urinaryInfectionId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getUrinaryInfectionId() {
        return urinaryInfectionId;
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
        UrinaryInfectionAnswer urinaryInfection = (UrinaryInfectionAnswer) o;
        return Objects.equals(id, urinaryInfection.id) &&
                Objects.equals(quizId, urinaryInfection.quizId) &&
                Objects.equals(urinaryInfectionId, urinaryInfection.urinaryInfectionId) &&
                Objects.equals(creationTimestamp, urinaryInfection.creationTimestamp) &&
                Objects.equals(modificationTimestamp, urinaryInfection.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, urinaryInfectionId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("urinaryInfectionId='" + urinaryInfectionId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
