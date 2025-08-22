package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class WeeklyTwelveAlcoholicDrinksAnswer {

    private final Long id;
    private final Long quizId;
    private final Long weeklyTwelveAlcoholicDrinksId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public WeeklyTwelveAlcoholicDrinksAnswer(
            Long id, Long quizId, Long weeklyTwelveAlcoholicDrinksId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.weeklyTwelveAlcoholicDrinksId = weeklyTwelveAlcoholicDrinksId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getWeeklyTwelveAlcoholicDrinksId() {
        return weeklyTwelveAlcoholicDrinksId;
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
        WeeklyTwelveAlcoholicDrinksAnswer weeklyTwelveAlcoholicDrinks = (WeeklyTwelveAlcoholicDrinksAnswer) o;
        return Objects.equals(id, weeklyTwelveAlcoholicDrinks.id) &&
                Objects.equals(quizId, weeklyTwelveAlcoholicDrinks.quizId) &&
                Objects.equals(weeklyTwelveAlcoholicDrinksId, weeklyTwelveAlcoholicDrinks.weeklyTwelveAlcoholicDrinksId) &&
                Objects.equals(creationTimestamp, weeklyTwelveAlcoholicDrinks.creationTimestamp) &&
                Objects.equals(modificationTimestamp, weeklyTwelveAlcoholicDrinks.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, weeklyTwelveAlcoholicDrinksId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("weeklyTwelveAlcoholicDrinksId='" + weeklyTwelveAlcoholicDrinksId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
