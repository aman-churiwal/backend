package ${package}.domain.dto;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class CategorizedAnswer {

    private final UUID quizExternalReference;
    private final Long categoryInternalId;

    public CategorizedAnswer(UUID quizExternalReference, Long categoryInternalId) {
        this.quizExternalReference = quizExternalReference;
        this.categoryInternalId = categoryInternalId;
    }

    public CategorizedAnswer(String quizExternalReference, Long categoryInternalId) {
        this.quizExternalReference = UUID.fromString(quizExternalReference);
        this.categoryInternalId = categoryInternalId;
    }

    public UUID getQuizExternalReference() {
        return quizExternalReference;
    }

    public Long getCategoryInternalId() {
        return categoryInternalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategorizedAnswer that = (CategorizedAnswer) o;
        return Objects.equals(quizExternalReference, that.quizExternalReference) &&
                Objects.equals(categoryInternalId, that.categoryInternalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizExternalReference, categoryInternalId);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CategorizedAnswer.class.getSimpleName() + "[", "]")
                .add("quizExternalReference=" + quizExternalReference)
                .add("categoryInternalId=" + categoryInternalId)
                .toString();
    }
}
