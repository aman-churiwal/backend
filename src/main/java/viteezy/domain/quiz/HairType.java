package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class HairType {

    private final Long id;
    private final String name;
    private final String code;
    private final boolean isActive;
    private final String subtitle;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public HairType(
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
        HairType hairType = (HairType) o;
        return Objects.equals(id, hairType.id) &&
                Objects.equals(name, hairType.name) &&
                Objects.equals(code, hairType.code) &&
                Objects.equals(isActive, hairType.isActive) &&
                Objects.equals(subtitle, hairType.subtitle) &&
                Objects.equals(creationTimestamp, hairType.creationTimestamp) &&
                Objects.equals(modificationTimestamp, hairType.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, isActive, subtitle, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HairType.class.getSimpleName() + "[", "]")
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
