package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class LoseWeightChallenge {

    private final Long id;
    private final String name;
    private final String code;
    private final boolean isActive;
    private final String subtitle;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public LoseWeightChallenge(
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

    public String getSubtitle() {
        return subtitle;
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
        LoseWeightChallenge that = (LoseWeightChallenge) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(code, that.code) &&
                Objects.equals(isActive, that.isActive) &&
                Objects.equals(subtitle, that.subtitle) &&
                Objects.equals(creationTimestamp, that.creationTimestamp) &&
                Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, isActive, subtitle, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LoseWeightChallenge.class.getSimpleName() + "[", "]")
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
