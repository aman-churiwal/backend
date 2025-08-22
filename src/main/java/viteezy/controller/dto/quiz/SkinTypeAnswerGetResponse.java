package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.SkinTypeAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SkinTypeAnswerGetResponse {

    private final Long id;
    private final Long skinTypeId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public SkinTypeAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "skinTypeId", required = true) Long skinTypeId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.skinTypeId = skinTypeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getSkinTypeId() {
        return skinTypeId;
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
        SkinTypeAnswerGetResponse skinTypeAnswerGetResponse = (SkinTypeAnswerGetResponse) o;
        return Objects.equals(id, skinTypeAnswerGetResponse.id) &&
                Objects.equals(skinTypeId, skinTypeAnswerGetResponse.skinTypeId) &&
                Objects.equals(creationTimestamp, skinTypeAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, skinTypeAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, skinTypeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SkinTypeAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("skinTypeId='" + skinTypeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static SkinTypeAnswerGetResponse from(SkinTypeAnswer that) {
        return new SkinTypeAnswerGetResponse(that.getId(), that.getSkinTypeId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
