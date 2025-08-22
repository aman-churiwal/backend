package ${package}.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ${package}.domain.quiz.${moduleNamePascalCase}Answer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ${moduleNamePascalCase}AnswerMapper implements RowMapper<${moduleNamePascalCase}Answer> {

    @Override
    public ${moduleNamePascalCase}Answer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long ${moduleNameCamelCase}Id = rs.getLong("${moduleNameSnakeCase}_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new ${moduleNamePascalCase}Answer(id, quizId, ${moduleNameCamelCase}Id, creationTimestamp, modificationTimestamp);
    }
}