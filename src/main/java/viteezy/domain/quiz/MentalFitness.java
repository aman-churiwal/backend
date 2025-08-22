package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class MentalFitness {

    private final Long id;
    private final String name;
    private final String code;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public MentalFitness(
            Long id, String name, String code, LocalDateTime creationTimestamp, LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.name = name;
        this.code = code;
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
        MentalFitness mentalFitness = (MentalFitness) o;
        return Objects.equals(id, mentalFitness.id) &&
                Objects.equals(name, mentalFitness.name) &&
                Objects.equals(code, mentalFitness.code) &&
                Objects.equals(creationTimestamp, mentalFitness.creationTimestamp) &&
                Objects.equals(modificationTimestamp, mentalFitness.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MentalFitness.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
