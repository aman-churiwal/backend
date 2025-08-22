package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.NameAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class NameAnswerMapper implements RowMapper<NameAnswer> {

    @Override
    public NameAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final String name = rs.getString("name");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new NameAnswer(id, quizId, name, creationTimestamp, modificationTimestamp);
    }
}