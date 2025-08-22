package ${package}.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import ${package}.domain.quiz.${moduleNamePascalCase}Answer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class ${moduleNamePascalCase}AnswerGetResponse {

    private final Long id;
    private final Long ${moduleNameCamelCase}Id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime creationTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime modificationTimestamp;

    @JsonCreator
    public ${moduleNamePascalCase}AnswerGetResponse(
            @JsonProperty(value = "id", required = true) Long id,
            @JsonProperty(value = "${moduleNameCamelCase}Id", required = true) Long ${moduleNameCamelCase}Id,
            @JsonProperty(value = "creationTimestamp", required = true) LocalDateTime creationTimestamp,
            @JsonProperty(value = "modificationTimestamp", required = true) LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.${moduleNameCamelCase}Id = ${moduleNameCamelCase}Id;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long get${moduleNamePascalCase}Id() {
        return ${moduleNameCamelCase}Id;
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
        ${moduleNamePascalCase}AnswerGetResponse ${moduleNameCamelCase}AnswerGetResponse = (${moduleNamePascalCase}AnswerGetResponse) o;
        return Objects.equals(id, ${moduleNameCamelCase}AnswerGetResponse.id) &&
                Objects.equals(${moduleNameCamelCase}Id, ${moduleNameCamelCase}AnswerGetResponse.${moduleNameCamelCase}Id) &&
                Objects.equals(creationTimestamp, ${moduleNameCamelCase}AnswerGetResponse.creationTimestamp) &&
                Objects.equals(modificationTimestamp, ${moduleNameCamelCase}AnswerGetResponse.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ${moduleNameCamelCase}Id, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ${moduleNamePascalCase}AnswerGetResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("${moduleNameCamelCase}Id='" + ${moduleNameCamelCase}Id + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }

    public static ${moduleNamePascalCase}AnswerGetResponse from(${moduleNamePascalCase}Answer that) {
        return new ${moduleNamePascalCase}AnswerGetResponse(that.getId(), that.get${moduleNamePascalCase}Id(), that.getCreationTimestamp(), that.getModificationTimestamp());
    }

}
