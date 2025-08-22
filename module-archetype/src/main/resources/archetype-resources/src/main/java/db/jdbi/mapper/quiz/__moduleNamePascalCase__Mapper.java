package ${package}.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import ${package}.domain.quiz.${moduleNamePascalCase};

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ${moduleNamePascalCase}Mapper implements RowMapper<${moduleNamePascalCase}> {

    @Override
    public ${moduleNamePascalCase} map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String name = rs.getString("name");
        final String code = rs.getString("code");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new ${moduleNamePascalCase}(id, name, code, creationTimestamp, modificationTimestamp);
    }
}