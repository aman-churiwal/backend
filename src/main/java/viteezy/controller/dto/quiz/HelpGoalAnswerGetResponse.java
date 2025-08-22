package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.HelpGoalAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class HelpGoalAnswerGetResponse {

    private final Long id;
    private final Long helpGoalId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public HelpGoalAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "helpGoalId", required = true) Long helpGoalId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.helpGoalId = helpGoalId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getHelpGoalId() {
        return helpGoalId;
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
        HelpGoalAnswerGetResponse helpGoalAnswerGetResponse = (HelpGoalAnswerGetResponse) o;
        return Objects.equals(id, helpGoalAnswerGetResponse.id) &&
                Objects.equals(helpGoalId, helpGoalAnswerGetResponse.helpGoalId) &&
                Objects.equals(creationTimestamp, helpGoalAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, helpGoalAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, helpGoalId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HelpGoalAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("helpGoalId='" + helpGoalId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static HelpGoalAnswerGetResponse from(HelpGoalAnswer that) {
        return new HelpGoalAnswerGetResponse(that.getId(), that.getHelpGoalId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
