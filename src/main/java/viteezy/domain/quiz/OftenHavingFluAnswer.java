package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class OftenHavingFluAnswer {

    private final Long id;
    private final Long quizId;
    private final Long oftenHavingFluId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public OftenHavingFluAnswer(
            Long id, Long quizId, Long oftenHavingFluId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.oftenHavingFluId = oftenHavingFluId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getOftenHavingFluId() {
        return oftenHavingFluId;
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
        OftenHavingFluAnswer oftenHavingFlu = (OftenHavingFluAnswer) o;
        return Objects.equals(id, oftenHavingFlu.id) &&
                Objects.equals(quizId, oftenHavingFlu.quizId) &&
                Objects.equals(oftenHavingFluId, oftenHavingFlu.oftenHavingFluId) &&
                Objects.equals(creationTimestamp, oftenHavingFlu.creationTimestamp) &&
                Objects.equals(modificationTimestamp, oftenHavingFlu.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, oftenHavingFluId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("oftenHavingFluId='" + oftenHavingFluId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
