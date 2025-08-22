package viteezy.domain.blend;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class Blend {

    private final Long id;
    private final BlendStatus status;
    private final UUID externalReference;
    private final Long customerId;
    private final Long quizId;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public Blend(Long id, BlendStatus status, UUID externalReference, Long customerId, Long quizId,
                 LocalDateTime creationTimestamp, LocalDateTime modificationTimestamp) {
        this.id = id;
        this.status = status;
        this.externalReference = externalReference;
        this.customerId = customerId;
        this.quizId = quizId;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public BlendStatus getStatus() {
        return status;
    }

    public UUID getExternalReference() {
        return externalReference;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getQuizId() {
        return quizId;
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
        Blend blend = (Blend) o;
        return Objects.equals(id, blend.id) &&
                status == blend.status &&
                Objects.equals(externalReference, blend.externalReference) &&
                Objects.equals(customerId, blend.customerId) &&
                Objects.equals(quizId, blend.quizId) &&
                Objects.equals(creationTimestamp, blend.creationTimestamp) &&
                Objects.equals(modificationTimestamp, blend.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, externalReference, customerId, quizId, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Blend.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("status=" + status)
                .add("externalReference=" + externalReference)
                .add("customerId=" + customerId)
                .add("quizId=" + quizId)
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
