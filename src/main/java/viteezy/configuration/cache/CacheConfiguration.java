package viteezy.configuration.cache;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.StringJoiner;

public class CacheConfiguration {

    private final Long ttlSeconds;
    private final Long maxSize;

    @JsonCreator
    public CacheConfiguration(
            @JsonProperty("ttl_seconds") Long ttlSeconds,
            @JsonProperty("max_size") Long maxSize
    ) {
        this.ttlSeconds = ttlSeconds;
        this.maxSize = maxSize;
    }

    public Long getTtlSeconds() {
        return ttlSeconds;
    }

    public Long getMaxSize() {
        return maxSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheConfiguration that = (CacheConfiguration) o;
        return Objects.equals(getTtlSeconds(), that.getTtlSeconds()) &&
                Objects.equals(getMaxSize(), that.getMaxSize());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTtlSeconds(), getMaxSize());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CacheConfiguration.class.getSimpleName() + "[", "]")
                .add("ttlSeconds=" + ttlSeconds)
                .add("maxSize=" + maxSize)
                .toString();
    }
}
