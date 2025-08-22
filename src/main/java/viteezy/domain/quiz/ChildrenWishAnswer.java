package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class ChildrenWishAnswer {

    private final Long id;
    private final Long quizId;
    private final Long childrenWishId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public ChildrenWishAnswer(
            Long id, Long quizId, Long childrenWishId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.childrenWishId = childrenWishId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getChildrenWishId() {
        return childrenWishId;
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
        ChildrenWishAnswer childrenWish = (ChildrenWishAnswer) o;
        return Objects.equals(id, childrenWish.id) &&
                Objects.equals(quizId, childrenWish.quizId) &&
                Objects.equals(childrenWishId, childrenWish.childrenWishId) &&
                Objects.equals(creationTimestamp, childrenWish.creationTimestamp) &&
                Objects.equals(modificationTimestamp, childrenWish.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, childrenWishId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("childrenWishId='" + childrenWishId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
