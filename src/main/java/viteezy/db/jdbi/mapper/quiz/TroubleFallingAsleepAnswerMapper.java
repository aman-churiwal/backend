package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.TroubleFallingAsleepAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TroubleFallingAsleepAnswerMapper implements RowMapper<TroubleFallingAsleepAnswer> {

    @Override
    public TroubleFallingAsleepAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long troubleFallingAsleepId = rs.getLong("trouble_falling_asleep_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new TroubleFallingAsleepAnswer(id, quizId, troubleFallingAsleepId, creationTimestamp, modificationTimestamp);
    }
}