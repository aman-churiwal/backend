package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.NailImprovementAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class NailImprovementAnswerGetResponse {

    private final Long id;
    private final Long nailImprovementId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public NailImprovementAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "nailImprovementId", required = true) Long nailImprovementId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.nailImprovementId = nailImprovementId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getNailImprovementId() {
        return nailImprovementId;
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
        NailImprovementAnswerGetResponse nailImprovementAnswerGetResponse = (NailImprovementAnswerGetResponse) o;
        return Objects.equals(id, nailImprovementAnswerGetResponse.id) &&
                Objects.equals(nailImprovementId, nailImprovementAnswerGetResponse.nailImprovementId) &&
                Objects.equals(creationTimestamp, nailImprovementAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, nailImprovementAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nailImprovementId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NailImprovementAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("nailImprovementId='" + nailImprovementId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static NailImprovementAnswerGetResponse from(NailImprovementAnswer that) {
        return new NailImprovementAnswerGetResponse(that.getId(), that.getNailImprovementId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
