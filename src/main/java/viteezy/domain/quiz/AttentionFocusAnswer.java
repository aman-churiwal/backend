package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AttentionFocusAnswer {

    private final Long id;
    private final Long quizId;
    private final Long attentionFocusId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public AttentionFocusAnswer(
            Long id, Long quizId, Long attentionFocusId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.attentionFocusId = attentionFocusId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getAttentionFocusId() {
        return attentionFocusId;
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
        AttentionFocusAnswer attentionFocus = (AttentionFocusAnswer) o;
        return Objects.equals(id, attentionFocus.id) &&
                Objects.equals(quizId, attentionFocus.quizId) &&
                Objects.equals(attentionFocusId, attentionFocus.attentionFocusId) &&
                Objects.equals(creationTimestamp, attentionFocus.creationTimestamp) &&
                Objects.equals(modificationTimestamp, attentionFocus.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, attentionFocusId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("attentionFocusId='" + attentionFocusId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
