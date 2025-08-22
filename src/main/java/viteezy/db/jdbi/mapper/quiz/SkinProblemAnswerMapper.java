package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.SkinProblemAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class SkinProblemAnswerMapper implements RowMapper<SkinProblemAnswer> {

    @Override
    public SkinProblemAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long skinProblemId = rs.getLong("skin_problem_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new SkinProblemAnswer(id, quizId, skinProblemId, creationTimestamp, modificationTimestamp);
    }
}