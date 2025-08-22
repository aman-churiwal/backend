package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class GenderAnswer {

    private final Long id;
    private final Long quizId;
    private final Long genderId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public GenderAnswer(
            Long id, Long quizId, Long genderId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.genderId = genderId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getGenderId() {
        return genderId;
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
        GenderAnswer gender = (GenderAnswer) o;
        return Objects.equals(id, gender.id) &&
                Objects.equals(quizId, gender.quizId) &&
                Objects.equals(genderId, gender.genderId) &&
                Objects.equals(creationTimestamp, gender.creationTimestamp) &&
                Objects.equals(modificationTimestamp, gender.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, genderId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("genderId='" + genderId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
