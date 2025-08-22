package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class PresentAtCrowdedPlaces {

    private final Long id;
    private final String name;
    private final String code;
    private final boolean isActive;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public PresentAtCrowdedPlaces(
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
        PresentAtCrowdedPlaces presentAtCrowdedPlaces = (PresentAtCrowdedPlaces) o;
        return Objects.equals(id, presentAtCrowdedPlaces.id) &&
                Objects.equals(name, presentAtCrowdedPlaces.name) &&
                Objects.equals(code, presentAtCrowdedPlaces.code) &&
                Objects.equals(isActive, presentAtCrowdedPlaces.isActive) &&
                Objects.equals(creationTimestamp, presentAtCrowdedPlaces.creationTimestamp) &&
                Objects.equals(modificationTimestamp, presentAtCrowdedPlaces.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, isActive, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PresentAtCrowdedPlaces.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("isActive=" + isActive)
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
