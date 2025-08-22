package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.TransitionPeriodComplaintsAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class TransitionPeriodComplaintsAnswerGetResponse {

    private final Long id;
    private final Long transitionPeriodComplaintsId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public TransitionPeriodComplaintsAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "transitionPeriodComplaintsId", required = true) Long transitionPeriodComplaintsId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.transitionPeriodComplaintsId = transitionPeriodComplaintsId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getTransitionPeriodComplaintsId() {
        return transitionPeriodComplaintsId;
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
        TransitionPeriodComplaintsAnswerGetResponse transitionPeriodComplaintsAnswerGetResponse = (TransitionPeriodComplaintsAnswerGetResponse) o;
        return Objects.equals(id, transitionPeriodComplaintsAnswerGetResponse.id) &&
                Objects.equals(transitionPeriodComplaintsId, transitionPeriodComplaintsAnswerGetResponse.transitionPeriodComplaintsId) &&
                Objects.equals(creationTimestamp, transitionPeriodComplaintsAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, transitionPeriodComplaintsAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transitionPeriodComplaintsId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransitionPeriodComplaintsAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("transitionPeriodComplaintsId='" + transitionPeriodComplaintsId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static TransitionPeriodComplaintsAnswerGetResponse from(TransitionPeriodComplaintsAnswer that) {
        return new TransitionPeriodComplaintsAnswerGetResponse(that.getId(), that.getTransitionPeriodComplaintsId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
