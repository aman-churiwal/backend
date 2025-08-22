package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class CurrentLibidoAnswer {

    private final Long id;
    private final Long quizId;
    private final Long currentLibidoId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public CurrentLibidoAnswer(
            Long id, Long quizId, Long currentLibidoId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.currentLibidoId = currentLibidoId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getCurrentLibidoId() {
        return currentLibidoId;
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
        CurrentLibidoAnswer currentLibido = (CurrentLibidoAnswer) o;
        return Objects.equals(id, currentLibido.id) &&
                Objects.equals(quizId, currentLibido.quizId) &&
                Objects.equals(currentLibidoId, currentLibido.currentLibidoId) &&
                Objects.equals(creationTimestamp, currentLibido.creationTimestamp) &&
                Objects.equals(modificationTimestamp, currentLibido.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, currentLibidoId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("currentLibidoId='" + currentLibidoId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
