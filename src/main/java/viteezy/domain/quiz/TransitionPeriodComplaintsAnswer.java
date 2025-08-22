package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class TransitionPeriodComplaintsAnswer {

    private final Long id;
    private final Long quizId;
    private final Long transitionPeriodComplaintsId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public TransitionPeriodComplaintsAnswer(
            Long id, Long quizId, Long transitionPeriodComplaintsId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.transitionPeriodComplaintsId = transitionPeriodComplaintsId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getTransitionPeriodComplaintsId() {
        return transitionPeriodComplaintsId;
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
        TransitionPeriodComplaintsAnswer transitionPeriodComplaints = (TransitionPeriodComplaintsAnswer) o;
        return Objects.equals(id, transitionPeriodComplaints.id) &&
                Objects.equals(quizId, transitionPeriodComplaints.quizId) &&
                Objects.equals(transitionPeriodComplaintsId, transitionPeriodComplaints.transitionPeriodComplaintsId) &&
                Objects.equals(creationTimestamp, transitionPeriodComplaints.creationTimestamp) &&
                Objects.equals(modificationTimestamp, transitionPeriodComplaints.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, transitionPeriodComplaintsId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("transitionPeriodComplaintsId='" + transitionPeriodComplaintsId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
