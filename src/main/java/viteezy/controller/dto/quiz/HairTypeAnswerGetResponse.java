package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.HairTypeAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class HairTypeAnswerGetResponse {

    private final Long id;
    private final Long hairTypeId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public HairTypeAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "hairTypeId", required = true) Long hairTypeId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.hairTypeId = hairTypeId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getHairTypeId() {
        return hairTypeId;
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
        HairTypeAnswerGetResponse hairTypeAnswerGetResponse = (HairTypeAnswerGetResponse) o;
        return Objects.equals(id, hairTypeAnswerGetResponse.id) &&
                Objects.equals(hairTypeId, hairTypeAnswerGetResponse.hairTypeId) &&
                Objects.equals(creationTimestamp, hairTypeAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, hairTypeAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hairTypeId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HairTypeAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("hairTypeId='" + hairTypeId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static HairTypeAnswerGetResponse from(HairTypeAnswer that) {
        return new HairTypeAnswerGetResponse(that.getId(), that.getHairTypeId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
