package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.BingeEatingAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class BingeEatingAnswerGetResponse {

    private final Long id;
    private final Long bingeEatingId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public BingeEatingAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "bingeEatingId", required = true) Long bingeEatingId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.bingeEatingId = bingeEatingId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getBingeEatingId() {
        return bingeEatingId;
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
        BingeEatingAnswerGetResponse bingeEatingAnswerGetResponse = (BingeEatingAnswerGetResponse) o;
        return Objects.equals(id, bingeEatingAnswerGetResponse.id) &&
                Objects.equals(bingeEatingId, bingeEatingAnswerGetResponse.bingeEatingId) &&
                Objects.equals(creationTimestamp, bingeEatingAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, bingeEatingAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bingeEatingId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BingeEatingAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("bingeEatingId='" + bingeEatingId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static BingeEatingAnswerGetResponse from(BingeEatingAnswer that) {
        return new BingeEatingAnswerGetResponse(that.getId(), that.getBingeEatingId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
