package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.NewProductAvailable;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class NewProductAvailableGetResponse {

    private final Long id;
    private final String name;
    private final String code;
    private final String subtitle;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public NewProductAvailableGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "code", required = true) String code,
            @JsonProperty(value = "subtitle", required = true) String subtitle,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.name = name;
        this.code = code;
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
        NewProductAvailableGetResponse that = (NewProductAvailableGetResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(code, that.code) &&
                Objects.equals(subtitle, that.subtitle) &&
                Objects.equals(creationTimestamp, that.creationTimestamp) &&
                Objects.equals(modificationTimestamp, that.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, subtitle, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NewProductAvailableGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("subtitle='" + subtitle + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static NewProductAvailableGetResponse from(NewProductAvailable that) {
        return new NewProductAvailableGetResponse(that.getId(), that.getName(), that.getCode(), that.getSubtitle(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
