package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.DailyFourCoffeeAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DailyFourCoffeeAnswerGetResponse {

    private final Long id;
    private final Long dailyFourCoffeeId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public DailyFourCoffeeAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "dailyFourCoffeeId", required = true) Long dailyFourCoffeeId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.dailyFourCoffeeId = dailyFourCoffeeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
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
        DailyFourCoffeeAnswerGetResponse dailyFourCoffeeAnswerGetResponse = (DailyFourCoffeeAnswerGetResponse) o;
        return Objects.equals(id, dailyFourCoffeeAnswerGetResponse.id) &&
                Objects.equals(dailyFourCoffeeId, dailyFourCoffeeAnswerGetResponse.dailyFourCoffeeId) &&
                Objects.equals(creationTimestamp, dailyFourCoffeeAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, dailyFourCoffeeAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dailyFourCoffeeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DailyFourCoffeeAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("dailyFourCoffeeId='" + dailyFourCoffeeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static DailyFourCoffeeAnswerGetResponse from(DailyFourCoffeeAnswer that) {
        return new DailyFourCoffeeAnswerGetResponse(that.getId(), that.getDailyFourCoffeeId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
