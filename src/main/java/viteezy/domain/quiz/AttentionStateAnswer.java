package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AttentionStateAnswer {

    private final Long id;
    private final Long quizId;
    private final Long attentionStateId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public AttentionStateAnswer(
            Long id, Long quizId, Long attentionStateId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.attentionStateId = attentionStateId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getAttentionStateId() {
        return attentionStateId;
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
        AttentionStateAnswer attentionState = (AttentionStateAnswer) o;
        return Objects.equals(id, attentionState.id) &&
                Objects.equals(quizId, attentionState.quizId) &&
                Objects.equals(attentionStateId, attentionState.attentionStateId) &&
                Objects.equals(creationTimestamp, attentionState.creationTimestamp) &&
                Objects.equals(modificationTimestamp, attentionState.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, attentionStateId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("attentionStateId='" + attentionStateId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
