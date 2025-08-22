package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.PrimaryGoalAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PrimaryGoalAnswerMapper implements RowMapper<PrimaryGoalAnswer> {

    @Override
    public PrimaryGoalAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long primaryGoalId = rs.getLong("primary_goal_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new PrimaryGoalAnswer(id, quizId, primaryGoalId, creationTimestamp, modificationTimestamp);
    }
}