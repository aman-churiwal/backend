package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class MenstruationSideIssueAnswer {

    private final Long id;
    private final Long quizId;
    private final Long menstruationSideIssueId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public MenstruationSideIssueAnswer(
            Long id, Long quizId, Long menstruationSideIssueId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.menstruationSideIssueId = menstruationSideIssueId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getMenstruationSideIssueId() {
        return menstruationSideIssueId;
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
        MenstruationSideIssueAnswer menstruationSideIssue = (MenstruationSideIssueAnswer) o;
        return Objects.equals(id, menstruationSideIssue.id) &&
                Objects.equals(quizId, menstruationSideIssue.quizId) &&
                Objects.equals(menstruationSideIssueId, menstruationSideIssue.menstruationSideIssueId) &&
                Objects.equals(creationTimestamp, menstruationSideIssue.creationTimestamp) &&
                Objects.equals(modificationTimestamp, menstruationSideIssue.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, menstruationSideIssueId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("menstruationSideIssueId='" + menstruationSideIssueId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
