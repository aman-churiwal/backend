package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.DigestionOccurrence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DigestionOccurrenceMapper implements RowMapper<DigestionOccurrence> {

    @Override
    public DigestionOccurrence map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String name = rs.getString("name");
        final String code = rs.getString("code");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        final boolean isActive = rs.getBoolean("is_active");
        return new DigestionOccurrence(id, name, code, isActive, creationTimestamp, modificationTimestamp);
    }
}