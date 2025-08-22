package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.StressLevelAtEndOfDayAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class StressLevelAtEndOfDayAnswerMapper implements RowMapper<StressLevelAtEndOfDayAnswer> {

    @Override
    public StressLevelAtEndOfDayAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long stressLevelAtEndOfDayId = rs.getLong("stress_level_at_end_of_day_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new StressLevelAtEndOfDayAnswer(id, quizId, stressLevelAtEndOfDayId, creationTimestamp, modificationTimestamp);
    }
}