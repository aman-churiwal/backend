package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.BirthHealthAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class BirthHealthAnswerGetResponse {

    private final Long id;
    private final Long birthHealthId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public BirthHealthAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "birthHealthId", required = true) Long birthHealthId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.birthHealthId = birthHealthId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getBirthHealthId() {
        return birthHealthId;
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
        BirthHealthAnswerGetResponse birthHealthAnswerGetResponse = (BirthHealthAnswerGetResponse) o;
        return Objects.equals(id, birthHealthAnswerGetResponse.id) &&
                Objects.equals(birthHealthId, birthHealthAnswerGetResponse.birthHealthId) &&
                Objects.equals(creationTimestamp, birthHealthAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, birthHealthAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, birthHealthId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BirthHealthAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("birthHealthId='" + birthHealthId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static BirthHealthAnswerGetResponse from(BirthHealthAnswer that) {
        return new BirthHealthAnswerGetResponse(that.getId(), that.getBirthHealthId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
