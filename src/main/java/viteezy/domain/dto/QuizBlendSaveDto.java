package viteezy.domain.dto;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class QuizBlendSaveDto {

    private final UUID quizExternalReference;

    public QuizBlendSaveDto(UUID quizExternalReference) {
        this.quizExternalReference = quizExternalReference;
    }

    public UUID getQuizExternalReference() {
        return quizExternalReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizBlendSaveDto that = (QuizBlendSaveDto) o;
        return Objects.equals(quizExternalReference, that.quizExternalReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizExternalReference);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", QuizBlendSaveDto.class.getSimpleName() + "[", "]")
                .add("quizExternalReference=" + quizExternalReference)
                .toString();
    }
}
