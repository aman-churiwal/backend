package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.PresentAtCrowdedPlacesAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class PresentAtCrowdedPlacesAnswerGetResponse {

    private final Long id;
    private final Long presentAtCrowdedPlacesId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public PresentAtCrowdedPlacesAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "presentAtCrowdedPlacesId", required = true) Long presentAtCrowdedPlacesId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.presentAtCrowdedPlacesId = presentAtCrowdedPlacesId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getPresentAtCrowdedPlacesId() {
        return presentAtCrowdedPlacesId;
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
        PresentAtCrowdedPlacesAnswerGetResponse presentAtCrowdedPlacesAnswerGetResponse = (PresentAtCrowdedPlacesAnswerGetResponse) o;
        return Objects.equals(id, presentAtCrowdedPlacesAnswerGetResponse.id) &&
                Objects.equals(presentAtCrowdedPlacesId, presentAtCrowdedPlacesAnswerGetResponse.presentAtCrowdedPlacesId) &&
                Objects.equals(creationTimestamp, presentAtCrowdedPlacesAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, presentAtCrowdedPlacesAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, presentAtCrowdedPlacesId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PresentAtCrowdedPlacesAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("presentAtCrowdedPlacesId='" + presentAtCrowdedPlacesId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static PresentAtCrowdedPlacesAnswerGetResponse from(PresentAtCrowdedPlacesAnswer that) {
        return new PresentAtCrowdedPlacesAnswerGetResponse(that.getId(), that.getPresentAtCrowdedPlacesId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
