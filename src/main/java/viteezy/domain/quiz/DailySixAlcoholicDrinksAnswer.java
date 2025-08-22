package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DailySixAlcoholicDrinksAnswer {

    private final Long id;
    private final Long quizId;
    private final Long dailySixAlcoholicDrinksId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public DailySixAlcoholicDrinksAnswer(
            Long id, Long quizId, Long dailySixAlcoholicDrinksId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.dailySixAlcoholicDrinksId = dailySixAlcoholicDrinksId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getDailySixAlcoholicDrinksId() {
        return dailySixAlcoholicDrinksId;
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
        DailySixAlcoholicDrinksAnswer dailySixAlcoholicDrinks = (DailySixAlcoholicDrinksAnswer) o;
        return Objects.equals(id, dailySixAlcoholicDrinks.id) &&
                Objects.equals(quizId, dailySixAlcoholicDrinks.quizId) &&
                Objects.equals(dailySixAlcoholicDrinksId, dailySixAlcoholicDrinks.dailySixAlcoholicDrinksId) &&
                Objects.equals(creationTimestamp, dailySixAlcoholicDrinks.creationTimestamp) &&
                Objects.equals(modificationTimestamp, dailySixAlcoholicDrinks.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, dailySixAlcoholicDrinksId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("dailySixAlcoholicDrinksId='" + dailySixAlcoholicDrinksId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
