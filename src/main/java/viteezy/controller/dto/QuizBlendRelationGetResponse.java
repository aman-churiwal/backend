package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.blend.Blend;
import viteezy.domain.quiz.Quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class QuizBlendRelationGetResponse {

    private final UUID quizExternalReference;
    private final UUID blendExternalReference;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public QuizBlendRelationGetResponse(
            @JsonProperty(value = "quizExternalReference", required = true) UUID quizExternalReference,
            @JsonProperty(value = "blendExternalReference", required = true) UUID blendExternalReference,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.quizExternalReference = quizExternalReference;
        this.blendExternalReference = blendExternalReference;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public static QuizBlendRelationGetResponse from(Quiz quiz, UUID blendExternalReference) {
        return new QuizBlendRelationGetResponse(
                quiz.getExternalReference(), blendExternalReference, quiz.getCreationDate(),
                quiz.getLastModified()
        );
    }

    public static QuizBlendRelationGetResponse from(Blend blend, UUID quizExternalReference) {
        return new QuizBlendRelationGetResponse(
                quizExternalReference, blend.getExternalReference(), blend.getCreationTimestamp(),
                blend.getModificationTimestamp()
        );
    }

    public UUID getQuizExternalReference() {
        return quizExternalReference;
    }

    public UUID getBlendExternalReference() {
        return blendExternalReference;
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
        if (!(o instanceof QuizBlendRelationGetResponse)) return false;
        QuizBlendRelationGetResponse that = (QuizBlendRelationGetResponse) o;
        return Objects.equals(quizExternalReference, that.quizExternalReference) && Objects.equals(blendExternalReference, that.blendExternalReference) && Objects.equals(creationTimestamp, that.creationTimestamp) && Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizExternalReference, blendExternalReference, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", QuizBlendRelationGetResponse.class.getSimpleName() + "[", "]")
                .add("quizExternalReference=" + quizExternalReference)
                .add("blendExternalReference=" + blendExternalReference)
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
