package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.CurrentLibidoAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class CurrentLibidoAnswerGetResponse {

    private final Long id;
    private final Long currentLibidoId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public CurrentLibidoAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "currentLibidoId", required = true) Long currentLibidoId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.currentLibidoId = currentLibidoId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getCurrentLibidoId() {
        return currentLibidoId;
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
        CurrentLibidoAnswerGetResponse currentLibidoAnswerGetResponse = (CurrentLibidoAnswerGetResponse) o;
        return Objects.equals(id, currentLibidoAnswerGetResponse.id) &&
                Objects.equals(currentLibidoId, currentLibidoAnswerGetResponse.currentLibidoId) &&
                Objects.equals(creationTimestamp, currentLibidoAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, currentLibidoAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currentLibidoId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CurrentLibidoAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("currentLibidoId='" + currentLibidoId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static CurrentLibidoAnswerGetResponse from(CurrentLibidoAnswer that) {
        return new CurrentLibidoAnswerGetResponse(that.getId(), that.getCurrentLibidoId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
