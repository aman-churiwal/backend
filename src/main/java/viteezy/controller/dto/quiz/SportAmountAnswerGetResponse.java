package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.SportAmountAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SportAmountAnswerGetResponse {

    private final Long id;
    private final Long sportAmountId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public SportAmountAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "sportAmountId", required = true) Long sportAmountId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.sportAmountId = sportAmountId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getSportAmountId() {
        return sportAmountId;
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
        SportAmountAnswerGetResponse sportAmountAnswerGetResponse = (SportAmountAnswerGetResponse) o;
        return Objects.equals(id, sportAmountAnswerGetResponse.id) &&
                Objects.equals(sportAmountId, sportAmountAnswerGetResponse.sportAmountId) &&
                Objects.equals(creationTimestamp, sportAmountAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, sportAmountAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sportAmountId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SportAmountAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("sportAmountId='" + sportAmountId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static SportAmountAnswerGetResponse from(SportAmountAnswer that) {
        return new SportAmountAnswerGetResponse(that.getId(), that.getSportAmountId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
