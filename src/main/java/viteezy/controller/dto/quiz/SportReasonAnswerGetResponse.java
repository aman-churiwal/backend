package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.SportReasonAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SportReasonAnswerGetResponse {

    private final Long id;
    private final Long sportReasonId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public SportReasonAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "sportReasonId", required = true) Long sportReasonId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.sportReasonId = sportReasonId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getSportReasonId() {
        return sportReasonId;
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
        SportReasonAnswerGetResponse sportReasonAnswerGetResponse = (SportReasonAnswerGetResponse) o;
        return Objects.equals(id, sportReasonAnswerGetResponse.id) &&
                Objects.equals(sportReasonId, sportReasonAnswerGetResponse.sportReasonId) &&
                Objects.equals(creationTimestamp, sportReasonAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, sportReasonAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sportReasonId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SportReasonAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("sportReasonId='" + sportReasonId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static SportReasonAnswerGetResponse from(SportReasonAnswer that) {
        return new SportReasonAnswerGetResponse(that.getId(), that.getSportReasonId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
