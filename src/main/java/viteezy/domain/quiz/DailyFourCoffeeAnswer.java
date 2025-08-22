package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DailyFourCoffeeAnswer {

    private final Long id;
    private final Long quizId;
    private final Long dailyFourCoffeeId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public DailyFourCoffeeAnswer(
            Long id, Long quizId, Long dailyFourCoffeeId, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.dailyFourCoffeeId = dailyFourCoffeeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getDailyFourCoffeeId() {
        return dailyFourCoffeeId;
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
        DailyFourCoffeeAnswer dailyFourCoffee = (DailyFourCoffeeAnswer) o;
        return Objects.equals(id, dailyFourCoffee.id) &&
                Objects.equals(quizId, dailyFourCoffee.quizId) &&
                Objects.equals(dailyFourCoffeeId, dailyFourCoffee.dailyFourCoffeeId) &&
                Objects.equals(creationTimestamp, dailyFourCoffee.creationTimestamp) &&
                Objects.equals(modificationTimestamp, dailyFourCoffee.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, dailyFourCoffeeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("dailyFourCoffeeId='" + dailyFourCoffeeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
