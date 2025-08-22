package viteezy.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class MenstruationSideIssue {

    private final Long id;
    private final String name;
    private final String code;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public MenstruationSideIssue(
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
        MenstruationSideIssue menstruationSideIssue = (MenstruationSideIssue) o;
        return Objects.equals(id, menstruationSideIssue.id) &&
                Objects.equals(name, menstruationSideIssue.name) &&
                Objects.equals(code, menstruationSideIssue.code) &&
                Objects.equals(creationTimestamp, menstruationSideIssue.creationTimestamp) &&
                Objects.equals(modificationTimestamp, menstruationSideIssue.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MenstruationSideIssue.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
