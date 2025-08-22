package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class LackOfConcentrationAnswer {

    private final Long id;
    private final Long quizId;
    private final Long lackOfConcentrationId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public LackOfConcentrationAnswer(
            Long id, Long quizId, Long lackOfConcentrationId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.lackOfConcentrationId = lackOfConcentrationId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getLackOfConcentrationId() {
        return lackOfConcentrationId;
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
        LackOfConcentrationAnswer lackOfConcentration = (LackOfConcentrationAnswer) o;
        return Objects.equals(id, lackOfConcentration.id) &&
                Objects.equals(quizId, lackOfConcentration.quizId) &&
                Objects.equals(lackOfConcentrationId, lackOfConcentration.lackOfConcentrationId) &&
                Objects.equals(creationTimestamp, lackOfConcentration.creationTimestamp) &&
                Objects.equals(modificationTimestamp, lackOfConcentration.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, lackOfConcentrationId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("lackOfConcentrationId='" + lackOfConcentrationId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
