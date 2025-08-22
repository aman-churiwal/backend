package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class EmailAnswer {

    private final Long id;
    private final Long quizId;
    private final String email;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;
    private final Boolean optIn;

    public EmailAnswer(
            Long id, Long quizId, String email, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp, Boolean optIn
    ) {
        this.id = id;
        this.quizId = quizId;
        this.email = email;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
        this.optIn = optIn;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public LocalDateTime getModificationTimestamp() {
        return modificationTimestamp;
    }

    public Boolean getOptIn() {
        return optIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailAnswer that = (EmailAnswer) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(quizId, that.quizId) &&
                Objects.equals(email, that.email) &&
                Objects.equals(creationTimestamp, that.creationTimestamp) &&
                Objects.equals(modificationTimestamp, that.modificationTimestamp) &&
                Objects.equals(optIn, that.optIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, email, creationTimestamp, modificationTimestamp, optIn);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EmailAnswer.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId=" + quizId)
                .add("email='" + email + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .add("optIn=" + optIn)
                .toString();
    }
}
