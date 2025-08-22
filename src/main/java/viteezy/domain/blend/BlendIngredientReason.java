package viteezy.domain.blend;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class BlendIngredientReason {
    public final Long id;
    private final BlendIngredientReasonCode code;
    private final String description;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime lastModificationTimestamp;

    public BlendIngredientReason(
            Long id, BlendIngredientReasonCode code, String description, LocalDateTime creationTimestamp,
            LocalDateTime lastModificationTimestamp
    ) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.creationTimestamp = creationTimestamp;
        this.lastModificationTimestamp = lastModificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public BlendIngredientReasonCode getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public LocalDateTime getLastModificationTimestamp() {
        return lastModificationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlendIngredientReason that = (BlendIngredientReason) o;
        return Objects.equals(id, that.id) &&
                code == that.code &&
                Objects.equals(description, that.description) &&
                Objects.equals(creationTimestamp, that.creationTimestamp) &&
                Objects.equals(lastModificationTimestamp, that.lastModificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, description, creationTimestamp, lastModificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BlendIngredientReason.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("code=" + code)
                .add("description='" + description + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("lastModificationTimestamp=" + lastModificationTimestamp)
                .toString();
    }
}
