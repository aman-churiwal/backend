package viteezy.domain.dashboard;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Note {

    private final Long id;
    private final Long fromId;
    private final Long customerId;
    private final String message;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public Note(Long id, Long fromId, Long customerId, String message, LocalDateTime creationTimestamp, LocalDateTime modificationTimestamp) {
        this.id = id;
        this.fromId = fromId;
        this.customerId = customerId;
        this.message = message;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getFromId() {
        return fromId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getMessage() {
        return message;
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
        Note note = (Note) o;
        return Objects.equals(id, note.id) && Objects.equals(fromId, note.fromId) && Objects.equals(customerId, note.customerId) && Objects.equals(message, note.message) && Objects.equals(creationTimestamp, note.creationTimestamp) && Objects.equals(modificationTimestamp, note.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromId, customerId, message, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Note.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("fromId=" + fromId)
                .add("customerId=" + customerId)
                .add("message='" + message + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}