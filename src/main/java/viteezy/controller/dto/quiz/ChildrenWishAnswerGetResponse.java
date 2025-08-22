package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.ChildrenWishAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class ChildrenWishAnswerGetResponse {

    private final Long id;
    private final Long childrenWishId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public ChildrenWishAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "childrenWishId", required = true) Long childrenWishId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.childrenWishId = childrenWishId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getChildrenWishId() {
        return childrenWishId;
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
        ChildrenWishAnswerGetResponse childrenWishAnswerGetResponse = (ChildrenWishAnswerGetResponse) o;
        return Objects.equals(id, childrenWishAnswerGetResponse.id) &&
                Objects.equals(childrenWishId, childrenWishAnswerGetResponse.childrenWishId) &&
                Objects.equals(creationTimestamp, childrenWishAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, childrenWishAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, childrenWishId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ChildrenWishAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("childrenWishId='" + childrenWishId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static ChildrenWishAnswerGetResponse from(ChildrenWishAnswer that) {
        return new ChildrenWishAnswerGetResponse(that.getId(), that.getChildrenWishId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
