package ${package}.domain.quiz;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class ${moduleNamePascalCase}Answer {

    private final Long id;
    private final Long quizId;
    private final Long ${moduleNameCamelCase}Id;
    private final LocalDateTime creationTimestamp;
    private final LocalDateTime modificationTimestamp;

    public ${moduleNamePascalCase}Answer(
            Long id, Long quizId, Long ${moduleNameCamelCase}Id, LocalDateTime creationTimestamp,
            LocalDateTime modificationTimestamp
    ) {
        this.id = id;
        this.quizId = quizId;
        this.${moduleNameCamelCase}Id = ${moduleNameCamelCase}Id;
        this.creationTimestamp = creationTimestamp;
        this.modificationTimestamp = modificationTimestamp;
    }

    public Long getId() {
        return id;
    }

    public Long getQuizId() {
        return quizId;
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
        ${moduleNamePascalCase}Answer ${moduleNameCamelCase} = (${moduleNamePascalCase}Answer) o;
        return Objects.equals(id, ${moduleNameCamelCase}.id) &&
                Objects.equals(quizId, ${moduleNameCamelCase}.quizId) &&
                Objects.equals(${moduleNameCamelCase}Id, ${moduleNameCamelCase}.${moduleNameCamelCase}Id) &&
                Objects.equals(creationTimestamp, ${moduleNameCamelCase}.creationTimestamp) &&
                Objects.equals(modificationTimestamp, ${moduleNameCamelCase}.modificationTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quizId, ${moduleNameCamelCase}Id, creationTimestamp, modificationTimestamp);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Module.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("quizId='" + quizId + "'")
                .add("${moduleNameCamelCase}Id='" + ${moduleNameCamelCase}Id + "'")
                .add("creationTimestamp=" + creationTimestamp)
                .add("modificationTimestamp=" + modificationTimestamp)
                .toString();
    }
}
