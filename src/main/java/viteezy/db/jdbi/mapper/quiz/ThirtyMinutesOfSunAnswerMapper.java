package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.ThirtyMinutesOfSunAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ThirtyMinutesOfSunAnswerMapper implements RowMapper<ThirtyMinutesOfSunAnswer> {

    @Override
    public ThirtyMinutesOfSunAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long thirtyMinutesOfSunId = rs.getLong("thirty_minutes_of_sun_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new ThirtyMinutesOfSunAnswer(id, quizId, thirtyMinutesOfSunId, creationTimestamp, modificationTimestamp);
    }
}