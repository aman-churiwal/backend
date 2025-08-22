package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class QuizPostResponse {

    private final UUID externalReference;
    private final Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime lastModified;

    @JsonCreator
    public QuizPostResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "externalReference", required = true) UUID externalReference,
            @JsonProperty(value = "creationDate", required = true) LocalDateTime creationDate,
            @JsonProperty(value = "lastModified", required = true) LocalDateTime lastModified
    ) {
        this.id = id;
        this.externalReference = externalReference;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
    }

    public static QuizPostResponse from(Quiz quiz) {
        return new QuizPostResponse(quiz.getId(), quiz.getExternalReference(), quiz.getCreationDate(), quiz.getLastModified()
        );
    }

    public UUID getExternalReference() {
        return externalReference;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizPostResponse quizPostResponse = (QuizPostResponse) o;
        return Objects.equals(id, quizPostResponse.id) &&
                Objects.equals(externalReference, quizPostResponse.externalReference) &&
                Objects.equals(creationDate, quizPostResponse.creationDate) &&
                Objects.equals(lastModified, quizPostResponse.lastModified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalReference, creationDate, lastModified);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", QuizPostResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("externalReference=" + externalReference)
                .add("creationDate=" + creationDate)
                .add("lastModified=" + lastModified)
                .toString();
    }
}
