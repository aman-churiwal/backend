package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.MenstruationSideIssueAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MenstruationSideIssueAnswerMapper implements RowMapper<MenstruationSideIssueAnswer> {

    @Override
    public MenstruationSideIssueAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long menstruationSideIssueId = rs.getLong("menstruation_side_issue_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new MenstruationSideIssueAnswer(id, quizId, menstruationSideIssueId, creationTimestamp, modificationTimestamp);
    }
}