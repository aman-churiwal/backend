package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.HealthyLifestyleAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class HealthyLifestyleAnswerGetResponse {

    private final Long id;
    private final Long healthyLifestyleId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public HealthyLifestyleAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "healthyLifestyleId", required = true) Long healthyLifestyleId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.healthyLifestyleId = healthyLifestyleId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getHealthyLifestyleId() {
        return healthyLifestyleId;
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
        HealthyLifestyleAnswerGetResponse healthyLifestyleAnswerGetResponse = (HealthyLifestyleAnswerGetResponse) o;
        return Objects.equals(id, healthyLifestyleAnswerGetResponse.id) &&
                Objects.equals(healthyLifestyleId, healthyLifestyleAnswerGetResponse.healthyLifestyleId) &&
                Objects.equals(creationTimestamp, healthyLifestyleAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, healthyLifestyleAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, healthyLifestyleId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HealthyLifestyleAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("healthyLifestyleId='" + healthyLifestyleId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static HealthyLifestyleAnswerGetResponse from(HealthyLifestyleAnswer that) {
        return new HealthyLifestyleAnswerGetResponse(that.getId(), that.getHealthyLifestyleId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
