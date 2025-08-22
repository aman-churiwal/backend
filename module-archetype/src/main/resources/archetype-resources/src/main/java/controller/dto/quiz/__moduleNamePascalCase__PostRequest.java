package ${package}.controller.dto.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ${package}.domain.quiz.${moduleNamePascalCase};

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class ${moduleNamePascalCase}PostRequest {

    private final String name;
    private final String code;

    @JsonCreator
    public ${moduleNamePascalCase}PostRequest(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "code", required = true) String code
    ) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ${moduleNamePascalCase}PostRequest ${moduleNameCamelCase}PostRequest = (${moduleNamePascalCase}PostRequest) o;
        return Objects.equals(name, ${moduleNameCamelCase}PostRequest.name) &&
        Objects.equals(code, ${moduleNameCamelCase}PostRequest.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ${moduleNamePascalCase}PostRequest.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("code='" + code + "'")
                .toString();
    }

    public ${moduleNamePascalCase} to() {
        return new ${moduleNamePascalCase}(null, this.name, this.code, LocalDateTime.now(), LocalDateTime.now());
    }
}