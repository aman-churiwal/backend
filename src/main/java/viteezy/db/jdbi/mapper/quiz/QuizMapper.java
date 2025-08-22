package viteezy.db.jdbi.mapper.quiz;

import io.vavr.control.Try;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.Quiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class QuizMapper implements RowMapper<Quiz> {

    @Override
    public Quiz map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final UUID externalReference = getExternalReference(rs);
        final LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();
        final LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
        final Long customerId = getNullableCustomerId(rs);
        final String utmContent = rs.getString("utm_content");
        return new Quiz(id, externalReference, creationDate, lastModified, customerId, utmContent);
    }

    private UUID getExternalReference(ResultSet rs) throws SQLException {
        String rawExternalReference = rs.getString("external_reference");
        return UUID.fromString(rawExternalReference);
    }

    private Long getNullableCustomerId(ResultSet rs) throws SQLException {
        return Try.success(rs.getLong("customer_id"))
                .filterTry(__ -> !rs.wasNull())
                .getOrNull();
    }
}
