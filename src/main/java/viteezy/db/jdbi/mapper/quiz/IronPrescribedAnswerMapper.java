package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.IronPrescribedAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class IronPrescribedAnswerMapper implements RowMapper<IronPrescribedAnswer> {

    @Override
    public IronPrescribedAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long ironPrescribedId = rs.getLong("iron_prescribed_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new IronPrescribedAnswer(id, quizId, ironPrescribedId, creationTimestamp, modificationTimestamp);
    }
}