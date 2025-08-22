package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class TiredWhenWakeUp {

    private final Long id;
    private final String name;
    private final String code;
    private final boolean isActive;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public TiredWhenWakeUp(
            Long id, String name, String code, Boolean isActive, LocalDateTime creationTimestamp, LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.isActive = isActive;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public boolean isActive() {
        return isActive;
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
        TiredWhenWakeUp tiredWhenWakeUp = (TiredWhenWakeUp) o;
        return Objects.equals(id, tiredWhenWakeUp.id) &&
                Objects.equals(name, tiredWhenWakeUp.name) &&
                Objects.equals(code, tiredWhenWakeUp.code) &&
                Objects.equals(isActive, tiredWhenWakeUp.isActive) &&
                Objects.equals(creationTimestamp, tiredWhenWakeUp.creationTimestamp) &&
                Objects.equals(modificationTimestamp, tiredWhenWakeUp.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, isActive, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TiredWhenWakeUp.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("isActive=" + isActive)
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
