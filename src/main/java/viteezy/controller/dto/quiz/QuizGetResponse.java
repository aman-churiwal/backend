package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class QuizGetResponse {

    private final UUID externalReference;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime lastModified;

    @JsonCreator
    public QuizGetResponse(
            @JsonProperty(value = "externalReference") UUID externalReference,
            @JsonProperty(value = "creationDate") LocalDateTime creationDate,
            @JsonProperty(value = "lastModified") LocalDateTime lastModified
    ) {
        this.externalReference = externalReference;
        this.creationDate = creationDate;
        this.lastModified = lastModified;
    }

    public static QuizGetResponse from(Quiz quiz) {
        return new QuizGetResponse(quiz.getExternalReference(), quiz.getCreationDate(), quiz.getLastModified()
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
        if (!(o instanceof QuizGetResponse)) return false;
        QuizGetResponse that = (QuizGetResponse) o;
        return Objects.equals(externalReference, that.externalReference) && Objects.equals(creationDate, that.creationDate) && Objects.equals(lastModified, that.lastModified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalReference, creationDate, lastModified);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", QuizGetResponse.class.getSimpleName() + "[", "]")
                .add("externalReference=" + externalReference)
                .add("creationDate=" + creationDate)
                .add("lastModified=" + lastModified)
                .toString();
    }
}
