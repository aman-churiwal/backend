package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.MenstruationMoodAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class MenstruationMoodAnswerGetResponse {

    private final Long id;
    private final Long menstruationMoodId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public MenstruationMoodAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "menstruationMoodId", required = true) Long menstruationMoodId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.menstruationMoodId = menstruationMoodId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getMenstruationMoodId() {
        return menstruationMoodId;
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
        MenstruationMoodAnswerGetResponse menstruationMoodAnswerGetResponse = (MenstruationMoodAnswerGetResponse) o;
        return Objects.equals(id, menstruationMoodAnswerGetResponse.id) &&
                Objects.equals(menstruationMoodId, menstruationMoodAnswerGetResponse.menstruationMoodId) &&
                Objects.equals(creationTimestamp, menstruationMoodAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, menstruationMoodAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menstruationMoodId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MenstruationMoodAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("menstruationMoodId='" + menstruationMoodId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static MenstruationMoodAnswerGetResponse from(MenstruationMoodAnswer that) {
        return new MenstruationMoodAnswerGetResponse(that.getId(), that.getMenstruationMoodId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
