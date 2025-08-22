package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.blend.Blend;
import viteezy.domain.blend.BlendStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class BlendMapper implements RowMapper<Blend> {

    @Override
    public Blend map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String statusString = rs.getString("status");
        final UUID externalReference = getExternalReference(rs);
        final BlendStatus status = BlendStatus.valueOf(statusString);
        final long customerId = rs.getLong("customer_id");
        final long quizId = rs.getLong("quiz_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new Blend(id, status, externalReference, customerId, quizId, creationTimestamp, modificationTimestamp);
    }

    private UUID getExternalReference(ResultSet rs) throws SQLException {
        String rawExternalReference = rs.getString("external_reference");
        return UUID.fromString(rawExternalReference);
    }
}