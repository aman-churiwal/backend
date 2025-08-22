package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.PregnancyState;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PregnancyStateMapper implements RowMapper<PregnancyState> {

    @Override
    public PregnancyState map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String name = rs.getString("name");
        final String code = rs.getString("code");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        final boolean isActive = rs.getBoolean("is_active");
        return new PregnancyState(id, name, code, isActive, creationTimestamp, modificationTimestamp);
    }
}