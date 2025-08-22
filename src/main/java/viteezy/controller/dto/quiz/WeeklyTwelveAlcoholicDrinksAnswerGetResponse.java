package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.WeeklyTwelveAlcoholicDrinksAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class WeeklyTwelveAlcoholicDrinksAnswerGetResponse {

    private final Long id;
    private final Long weeklyTwelveAlcoholicDrinksId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public WeeklyTwelveAlcoholicDrinksAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "weeklyTwelveAlcoholicDrinksId", required = true) Long weeklyTwelveAlcoholicDrinksId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.weeklyTwelveAlcoholicDrinksId = weeklyTwelveAlcoholicDrinksId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
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
        WeeklyTwelveAlcoholicDrinksAnswerGetResponse weeklyTwelveAlcoholicDrinksAnswerGetResponse = (WeeklyTwelveAlcoholicDrinksAnswerGetResponse) o;
        return Objects.equals(id, weeklyTwelveAlcoholicDrinksAnswerGetResponse.id) &&
                Objects.equals(weeklyTwelveAlcoholicDrinksId, weeklyTwelveAlcoholicDrinksAnswerGetResponse.weeklyTwelveAlcoholicDrinksId) &&
                Objects.equals(creationTimestamp, weeklyTwelveAlcoholicDrinksAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, weeklyTwelveAlcoholicDrinksAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, weeklyTwelveAlcoholicDrinksId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WeeklyTwelveAlcoholicDrinksAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("weeklyTwelveAlcoholicDrinksId='" + weeklyTwelveAlcoholicDrinksId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static WeeklyTwelveAlcoholicDrinksAnswerGetResponse from(WeeklyTwelveAlcoholicDrinksAnswer that) {
        return new WeeklyTwelveAlcoholicDrinksAnswerGetResponse(that.getId(), that.getWeeklyTwelveAlcoholicDrinksId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
