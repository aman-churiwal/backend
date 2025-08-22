package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class NameAnswer {

    private final Long id;
    private final Long quizId;
    private final String name;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public NameAnswer(
            Long id, Long quizId, String name, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.name = name;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public String getName() {
        return name;
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
        NameAnswer name = (NameAnswer) o;
        return Objects.equals(id, name.id) &&
                Objects.equals(quizId, name.quizId) &&
                Objects.equals(this.name, name.name) &&
                Objects.equals(creationTimestamp, name.creationTimestamp) &&
                Objects.equals(modificationTimestamp, name.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, name, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("name='" + name + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
