package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.OftenHavingFluAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class OftenHavingFluAnswerGetResponse {

    private final Long id;
    private final Long oftenHavingFluId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public OftenHavingFluAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "oftenHavingFluId", required = true) Long oftenHavingFluId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.oftenHavingFluId = oftenHavingFluId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getOftenHavingFluId() {
        return oftenHavingFluId;
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
        OftenHavingFluAnswerGetResponse oftenHavingFluAnswerGetResponse = (OftenHavingFluAnswerGetResponse) o;
        return Objects.equals(id, oftenHavingFluAnswerGetResponse.id) &&
                Objects.equals(oftenHavingFluId, oftenHavingFluAnswerGetResponse.oftenHavingFluId) &&
                Objects.equals(creationTimestamp, oftenHavingFluAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, oftenHavingFluAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, oftenHavingFluId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OftenHavingFluAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("oftenHavingFluId='" + oftenHavingFluId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static OftenHavingFluAnswerGetResponse from(OftenHavingFluAnswer that) {
        return new OftenHavingFluAnswerGetResponse(that.getId(), that.getOftenHavingFluId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
