package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class WeeklyTwelveAlcoholicDrinks {

    private final Long id;
    private final String name;
    private final String code;
    private final boolean isActive;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public WeeklyTwelveAlcoholicDrinks(
            Long id, String name, String code, Boolean isActive, LocalDateTime creationTimestamp, LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.isActive = isActive;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public boolean isActive() {
        return isActive;
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
        WeeklyTwelveAlcoholicDrinks weeklyTwelveAlcoholicDrinks = (WeeklyTwelveAlcoholicDrinks) o;
        return Objects.equals(id, weeklyTwelveAlcoholicDrinks.id) &&
                Objects.equals(name, weeklyTwelveAlcoholicDrinks.name) &&
                Objects.equals(code, weeklyTwelveAlcoholicDrinks.code) &&
                Objects.equals(isActive, weeklyTwelveAlcoholicDrinks.isActive) &&
                Objects.equals(creationTimestamp, weeklyTwelveAlcoholicDrinks.creationTimestamp) &&
                Objects.equals(modificationTimestamp, weeklyTwelveAlcoholicDrinks.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, isActive, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WeeklyTwelveAlcoholicDrinks.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("isActive=" + isActive)
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
