package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.HealthComplaintsAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class HealthComplaintsAnswerGetResponse {

    private final Long id;
    private final Long healthComplaintsId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public HealthComplaintsAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "healthComplaintsId", required = true) Long healthComplaintsId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.healthComplaintsId = healthComplaintsId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getHealthComplaintsId() {
        return healthComplaintsId;
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
        HealthComplaintsAnswerGetResponse healthComplaintsAnswerGetResponse = (HealthComplaintsAnswerGetResponse) o;
        return Objects.equals(id, healthComplaintsAnswerGetResponse.id) &&
                Objects.equals(healthComplaintsId, healthComplaintsAnswerGetResponse.healthComplaintsId) &&
                Objects.equals(creationTimestamp, healthComplaintsAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, healthComplaintsAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, healthComplaintsId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HealthComplaintsAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("healthComplaintsId='" + healthComplaintsId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static HealthComplaintsAnswerGetResponse from(HealthComplaintsAnswer that) {
        return new HealthComplaintsAnswerGetResponse(that.getId(), that.getHealthComplaintsId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
