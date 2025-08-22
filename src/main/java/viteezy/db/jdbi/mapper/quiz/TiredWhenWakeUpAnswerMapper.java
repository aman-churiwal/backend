package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.TiredWhenWakeUpAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TiredWhenWakeUpAnswerMapper implements RowMapper<TiredWhenWakeUpAnswer> {

    @Override
    public TiredWhenWakeUpAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long tiredWhenWakeUpId = rs.getLong("tired_when_wake_up_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new TiredWhenWakeUpAnswer(id, quizId, tiredWhenWakeUpId, creationTimestamp, modificationTimestamp);
    }
}