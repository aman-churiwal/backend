package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.UsageGoalAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class UsageGoalAnswerGetResponse {

    private final Long id;
    private final Long usageGoalId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public UsageGoalAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "usageGoalId", required = true) Long usageGoalId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.usageGoalId = usageGoalId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getUsageGoalId() {
        return usageGoalId;
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
        UsageGoalAnswerGetResponse usageGoalAnswerGetResponse = (UsageGoalAnswerGetResponse) o;
        return Objects.equals(id, usageGoalAnswerGetResponse.id) &&
                Objects.equals(usageGoalId, usageGoalAnswerGetResponse.usageGoalId) &&
                Objects.equals(creationTimestamp, usageGoalAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, usageGoalAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usageGoalId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UsageGoalAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("usageGoalId='" + usageGoalId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static UsageGoalAnswerGetResponse from(UsageGoalAnswer that) {
        return new UsageGoalAnswerGetResponse(that.getId(), that.getUsageGoalId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
