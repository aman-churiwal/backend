package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.LibidoStressLevelAnswer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class LibidoStressLevelAnswerGetResponse {

    private final Long id;
    private final Long libidoStressLevelId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public LibidoStressLevelAnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "libidoStressLevelId", required = true) Long libidoStressLevelId,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.libidoStressLevelId = libidoStressLevelId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getLibidoStressLevelId() {
        return libidoStressLevelId;
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
        LibidoStressLevelAnswerGetResponse libidoStressLevelAnswerGetResponse = (LibidoStressLevelAnswerGetResponse) o;
        return Objects.equals(id, libidoStressLevelAnswerGetResponse.id) &&
                Objects.equals(libidoStressLevelId, libidoStressLevelAnswerGetResponse.libidoStressLevelId) &&
                Objects.equals(creationTimestamp, libidoStressLevelAnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, libidoStressLevelAnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libidoStressLevelId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LibidoStressLevelAnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("libidoStressLevelId='" + libidoStressLevelId + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static LibidoStressLevelAnswerGetResponse from(LibidoStressLevelAnswer that) {
        return new LibidoStressLevelAnswerGetResponse(that.getId(), that.getLibidoStressLevelId(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
