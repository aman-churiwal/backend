package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class NewProductAvailableAnswer {

    private final Long id;
    private final Long quizId;
    private final Long newProductAvailableId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public NewProductAvailableAnswer(
            Long id, Long quizId, Long newProductAvailableId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.newProductAvailableId = newProductAvailableId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getNewProductAvailableId() {
        return newProductAvailableId;
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
        NewProductAvailableAnswer newProductAvailable = (NewProductAvailableAnswer) o;
        return Objects.equals(id, newProductAvailable.id) &&
                Objects.equals(quizId, newProductAvailable.quizId) &&
                Objects.equals(newProductAvailableId, newProductAvailable.newProductAvailableId) &&
                Objects.equals(creationTimestamp, newProductAvailable.creationTimestamp) &&
                Objects.equals(modificationTimestamp, newProductAvailable.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, newProductAvailableId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("newProductAvailableId='" + newProductAvailableId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
