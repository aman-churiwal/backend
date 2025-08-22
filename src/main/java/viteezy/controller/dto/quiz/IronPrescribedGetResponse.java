package viteezy.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import viteezy.domain.quiz.IronPrescribed;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class IronPrescribedGetResponse {

    private final Long id;
    private final String name;
    private final String code;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public IronPrescribedGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "code", required = true) String code,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
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
        IronPrescribedGetResponse ironPrescribedGetResponse = (IronPrescribedGetResponse) o;
        return Objects.equals(id, ironPrescribedGetResponse.id) &&
                Objects.equals(name, ironPrescribedGetResponse.name) &&
                Objects.equals(code, ironPrescribedGetResponse.code) &&
                Objects.equals(creationTimestamp, ironPrescribedGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, ironPrescribedGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", IronPrescribedGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static IronPrescribedGetResponse from(IronPrescribed that) {
        return new IronPrescribedGetResponse(that.getId(), that.getName(), that.getCode(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
