package viteezy.db.jdbi.mapper.dashboard;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.dashboard.Note;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class NoteMapper implements RowMapper<Note>  {

    @Override
    public Note map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final Long fromId = rs.getLong("from_id");
        final Long customerId = rs.getLong("customer_id");
        final String message = rs.getString("message");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new Note(id, fromId, customerId, message, creationTimestamp, modificationTimestamp);
    }
}