package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.MenstruationInterval;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MenstruationIntervalMapper implements RowMapper<MenstruationInterval> {

    @Override
    public MenstruationInterval map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String name = rs.getString("name");
        final String code = rs.getString("code");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new MenstruationInterval(id, name, code, creationTimestamp, modificationTimestamp);
    }
}