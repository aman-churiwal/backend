package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class EnergyStateAnswer {

    private final Long id;
    private final Long quizId;
    private final Long energyStateId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public EnergyStateAnswer(
            Long id, Long quizId, Long energyStateId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.energyStateId = energyStateId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getEnergyStateId() {
        return energyStateId;
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
        EnergyStateAnswer energyState = (EnergyStateAnswer) o;
        return Objects.equals(id, energyState.id) &&
                Objects.equals(quizId, energyState.quizId) &&
                Objects.equals(energyStateId, energyState.energyStateId) &&
                Objects.equals(creationTimestamp, energyState.creationTimestamp) &&
                Objects.equals(modificationTimestamp, energyState.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, energyStateId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("energyStateId='" + energyStateId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
