package viteezy.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class ConfigurationDatabaseObject {

    private final Long id;
    private final String name;
    private final String type;
    private final String value;
    private final LocalDateTime expirationTimestamp;
    private final LocalDateTime modificationTimestamp;
    private final LocalDateTime creationTimestamp;

    public ConfigurationDatabaseObject(Long id, String name, String type, String value, LocalDateTime expirationTimestamp,
                                       LocalDateTime modificationTimestamp, LocalDateTime creationTimestamp) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
        this.expirationTimestamp = expirationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
        this.creationTimestamp = creationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public LocalDateTime getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public LocalDateTime getModificationTimestamp() {
        return modificationTimestamp;
    }

    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurationDatabaseObject that = (ConfigurationDatabaseObject) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(value, that.value) &&
                Objects.equals(expirationTimestamp, that.expirationTimestamp) &&
                Objects.equals(modificationTimestamp, that.modificationTimestamp) &&
                Objects.equals(creationTimestamp, that.creationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, value, expirationTimestamp, modificationTimestamp, creationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConfigurationDatabaseObject.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("type='" + type + "'")
                .add("value='" + value + "'")
                .add("expirationTimestamp=" + expirationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .add("creationTimestamp=" + creationTimestamp)
                .toString();
    }
}
