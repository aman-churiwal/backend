package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class LibidoStressLevelAnswer {

    private final Long id;
    private final Long quizId;
    private final Long libidoStressLevelId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public LibidoStressLevelAnswer(
            Long id, Long quizId, Long libidoStressLevelId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.libidoStressLevelId = libidoStressLevelId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getLibidoStressLevelId() {
        return libidoStressLevelId;
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
        LibidoStressLevelAnswer libidoStressLevel = (LibidoStressLevelAnswer) o;
        return Objects.equals(id, libidoStressLevel.id) &&
                Objects.equals(quizId, libidoStressLevel.quizId) &&
                Objects.equals(libidoStressLevelId, libidoStressLevel.libidoStressLevelId) &&
                Objects.equals(creationTimestamp, libidoStressLevel.creationTimestamp) &&
                Objects.equals(modificationTimestamp, libidoStressLevel.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, libidoStressLevelId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("libidoStressLevelId='" + libidoStressLevelId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
