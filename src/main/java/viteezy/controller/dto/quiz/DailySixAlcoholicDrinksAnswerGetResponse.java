package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.DailySixAlcoholicDrinksAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DailySixAlcoholicDrinksAnswerGetResponse {

    private final Long id;
    private final Long dailySixAlcoholicDrinksId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public DailySixAlcoholicDrinksAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "dailySixAlcoholicDrinksId", required = true) Long dailySixAlcoholicDrinksId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.dailySixAlcoholicDrinksId = dailySixAlcoholicDrinksId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
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
        DailySixAlcoholicDrinksAnswerGetResponse dailySixAlcoholicDrinksAnswerGetResponse = (DailySixAlcoholicDrinksAnswerGetResponse) o;
        return Objects.equals(id, dailySixAlcoholicDrinksAnswerGetResponse.id) &&
                Objects.equals(dailySixAlcoholicDrinksId, dailySixAlcoholicDrinksAnswerGetResponse.dailySixAlcoholicDrinksId) &&
                Objects.equals(creationTimestamp, dailySixAlcoholicDrinksAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, dailySixAlcoholicDrinksAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dailySixAlcoholicDrinksId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DailySixAlcoholicDrinksAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("dailySixAlcoholicDrinksId='" + dailySixAlcoholicDrinksId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static DailySixAlcoholicDrinksAnswerGetResponse from(DailySixAlcoholicDrinksAnswer that) {
        return new DailySixAlcoholicDrinksAnswerGetResponse(that.getId(), that.getDailySixAlcoholicDrinksId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
