package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SkinType {

    private final Long id;
    private final String name;
    private final String code;
    private final boolean isActive;
    private final String subtitle;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public SkinType(
            Long id, String name, String code, Boolean isActive, String subtitle, LocalDateTime creationTimestamp, LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.isActive = isActive;
        this.subtitle = subtitle;
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

    public String getSubtitle() {
        return subtitle;
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
        SkinType skinType = (SkinType) o;
        return Objects.equals(id, skinType.id) &&
                Objects.equals(name, skinType.name) &&
                Objects.equals(code, skinType.code) &&
                Objects.equals(isActive, skinType.isActive) &&
                Objects.equals(subtitle, skinType.subtitle) &&
                Objects.equals(creationTimestamp, skinType.creationTimestamp) &&
                Objects.equals(modificationTimestamp, skinType.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, isActive, subtitle, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SkinType.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("isActive=" + isActive)
                .add("subtitle='" + subtitle + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
