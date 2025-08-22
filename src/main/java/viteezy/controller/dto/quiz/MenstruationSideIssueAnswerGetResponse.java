package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.MenstruationSideIssueAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class MenstruationSideIssueAnswerGetResponse {

    private final Long id;
    private final Long menstruationSideIssueId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public MenstruationSideIssueAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "menstruationSideIssueId", required = true) Long menstruationSideIssueId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.menstruationSideIssueId = menstruationSideIssueId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getMenstruationSideIssueId() {
        return menstruationSideIssueId;
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
        MenstruationSideIssueAnswerGetResponse menstruationSideIssueAnswerGetResponse = (MenstruationSideIssueAnswerGetResponse) o;
        return Objects.equals(id, menstruationSideIssueAnswerGetResponse.id) &&
                Objects.equals(menstruationSideIssueId, menstruationSideIssueAnswerGetResponse.menstruationSideIssueId) &&
                Objects.equals(creationTimestamp, menstruationSideIssueAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, menstruationSideIssueAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menstruationSideIssueId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MenstruationSideIssueAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("menstruationSideIssueId='" + menstruationSideIssueId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static MenstruationSideIssueAnswerGetResponse from(MenstruationSideIssueAnswer that) {
        return new MenstruationSideIssueAnswerGetResponse(that.getId(), that.getMenstruationSideIssueId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
