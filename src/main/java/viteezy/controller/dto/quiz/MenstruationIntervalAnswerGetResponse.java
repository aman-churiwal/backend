package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.MenstruationIntervalAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class MenstruationIntervalAnswerGetResponse {

    private final Long id;
    private final Long menstruationIntervalId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public MenstruationIntervalAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "menstruationIntervalId", required = true) Long menstruationIntervalId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.menstruationIntervalId = menstruationIntervalId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getMenstruationIntervalId() {
        return menstruationIntervalId;
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
        MenstruationIntervalAnswerGetResponse menstruationIntervalAnswerGetResponse = (MenstruationIntervalAnswerGetResponse) o;
        return Objects.equals(id, menstruationIntervalAnswerGetResponse.id) &&
                Objects.equals(menstruationIntervalId, menstruationIntervalAnswerGetResponse.menstruationIntervalId) &&
                Objects.equals(creationTimestamp, menstruationIntervalAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, menstruationIntervalAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menstruationIntervalId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MenstruationIntervalAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("menstruationIntervalId='" + menstruationIntervalId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static MenstruationIntervalAnswerGetResponse from(MenstruationIntervalAnswer that) {
        return new MenstruationIntervalAnswerGetResponse(that.getId(), that.getMenstruationIntervalId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
