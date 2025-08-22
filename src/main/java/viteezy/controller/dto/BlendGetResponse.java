package viteezy.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.blend.Blend;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class BlendGetResponse {

    private final String status;
    private final UUID externalReference;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public BlendGetResponse(
            @JsonProperty(value = "status", required = true) String status,
            @JsonProperty(value = "externalReference", required = true) UUID externalReference,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.status = status;
        this.externalReference = externalReference;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public static BlendGetResponse from(Blend that) {
        return new BlendGetResponse(
                that.getStatus().name(), that.getExternalReference(), that.getCreationTimestamp(),
                that.getModificationTimestamp()
        );
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public LocalDateTime getModificationTimestamp() {
        return modificationTimestamp;
    }

    public UUID getExternalReference() {
        return externalReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlendGetResponse)) return false;
        BlendGetResponse that = (BlendGetResponse) o;
        return Objects.equals(status, that.status) && Objects.equals(externalReference, that.externalReference) && Objects.equals(creationTimestamp, that.creationTimestamp) && Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, externalReference, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BlendGetResponse.class.getSimpleName() + "[", "]")
                .add("status='" + status + "'")
                .add("externalReference=" + externalReference)
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
