package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.BingeEatingReasonAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class BingeEatingReasonAnswerGetResponse {

    private final Long id;
    private final Long bingeEatingReasonId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public BingeEatingReasonAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "bingeEatingReasonId", required = true) Long bingeEatingReasonId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.bingeEatingReasonId = bingeEatingReasonId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getBingeEatingReasonId() {
        return bingeEatingReasonId;
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
        BingeEatingReasonAnswerGetResponse bingeEatingReasonAnswerGetResponse = (BingeEatingReasonAnswerGetResponse) o;
        return Objects.equals(id, bingeEatingReasonAnswerGetResponse.id) &&
                Objects.equals(bingeEatingReasonId, bingeEatingReasonAnswerGetResponse.bingeEatingReasonId) &&
                Objects.equals(creationTimestamp, bingeEatingReasonAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, bingeEatingReasonAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bingeEatingReasonId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BingeEatingReasonAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("bingeEatingReasonId='" + bingeEatingReasonId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static BingeEatingReasonAnswerGetResponse from(BingeEatingReasonAnswer that) {
        return new BingeEatingReasonAnswerGetResponse(that.getId(), that.getBingeEatingReasonId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
