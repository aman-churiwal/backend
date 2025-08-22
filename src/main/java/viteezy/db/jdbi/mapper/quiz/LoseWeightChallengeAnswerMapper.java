package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.LoseWeightChallengeAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LoseWeightChallengeAnswerMapper implements RowMapper<LoseWeightChallengeAnswer> {

    @Override
    public LoseWeightChallengeAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long loseWeightChallengeId = rs.getLong("lose_weight_challenge_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new LoseWeightChallengeAnswer(id, quizId, loseWeightChallengeId, creationTimestamp, modificationTimestamp);
    }
}