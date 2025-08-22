package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class SleepHoursLessThanSeven {

    private final Long id;
    private final String name;
    private final String code;
    private final boolean isActive;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public SleepHoursLessThanSeven(
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
        SleepHoursLessThanSeven sleepHoursLessThanSeven = (SleepHoursLessThanSeven) o;
        return Objects.equals(id, sleepHoursLessThanSeven.id) &&
                Objects.equals(name, sleepHoursLessThanSeven.name) &&
                Objects.equals(code, sleepHoursLessThanSeven.code) &&
                Objects.equals(isActive, sleepHoursLessThanSeven.isActive) &&
                Objects.equals(creationTimestamp, sleepHoursLessThanSeven.creationTimestamp) &&
                Objects.equals(modificationTimestamp, sleepHoursLessThanSeven.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, isActive, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SleepHoursLessThanSeven.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("isActive=" + isActive)
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
