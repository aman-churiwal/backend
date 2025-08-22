package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.Logging;
import viteezy.domain.LoggingEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LoggingMapper implements RowMapper<Logging>  {

    @Override
    public Logging map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final Long customerId = rs.getLong("customer_id");
        final LoggingEvent event = LoggingEvent.valueOf(rs.getString("event"));
        final String info = rs.getString("info");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        return new Logging(id, customerId, event, info, creationTimestamp);
    }
}