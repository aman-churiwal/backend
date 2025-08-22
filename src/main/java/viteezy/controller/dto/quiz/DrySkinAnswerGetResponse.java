package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.DrySkinAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class DrySkinAnswerGetResponse {

    private final Long id;
    private final Long drySkinId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public DrySkinAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "drySkinId", required = true) Long drySkinId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.drySkinId = drySkinId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getDrySkinId() {
        return drySkinId;
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
        DrySkinAnswerGetResponse drySkinAnswerGetResponse = (DrySkinAnswerGetResponse) o;
        return Objects.equals(id, drySkinAnswerGetResponse.id) &&
                Objects.equals(drySkinId, drySkinAnswerGetResponse.drySkinId) &&
                Objects.equals(creationTimestamp, drySkinAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, drySkinAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, drySkinId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DrySkinAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("drySkinId='" + drySkinId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static DrySkinAnswerGetResponse from(DrySkinAnswer that) {
        return new DrySkinAnswerGetResponse(that.getId(), that.getDrySkinId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
