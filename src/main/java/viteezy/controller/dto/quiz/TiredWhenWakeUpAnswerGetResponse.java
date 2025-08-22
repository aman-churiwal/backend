package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.TiredWhenWakeUpAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class TiredWhenWakeUpAnswerGetResponse {

    private final Long id;
    private final Long tiredWhenWakeUpId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public TiredWhenWakeUpAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "tiredWhenWakeUpId", required = true) Long tiredWhenWakeUpId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.tiredWhenWakeUpId = tiredWhenWakeUpId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getTiredWhenWakeUpId() {
        return tiredWhenWakeUpId;
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
        TiredWhenWakeUpAnswerGetResponse tiredWhenWakeUpAnswerGetResponse = (TiredWhenWakeUpAnswerGetResponse) o;
        return Objects.equals(id, tiredWhenWakeUpAnswerGetResponse.id) &&
                Objects.equals(tiredWhenWakeUpId, tiredWhenWakeUpAnswerGetResponse.tiredWhenWakeUpId) &&
                Objects.equals(creationTimestamp, tiredWhenWakeUpAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, tiredWhenWakeUpAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tiredWhenWakeUpId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TiredWhenWakeUpAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("tiredWhenWakeUpId='" + tiredWhenWakeUpId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static TiredWhenWakeUpAnswerGetResponse from(TiredWhenWakeUpAnswer that) {
        return new TiredWhenWakeUpAnswerGetResponse(that.getId(), that.getTiredWhenWakeUpId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
