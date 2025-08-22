package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.GenderAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class GenderAnswerMapper implements RowMapper<GenderAnswer> {

    @Override
    public GenderAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long genderId = rs.getLong("gender_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new GenderAnswer(id, quizId, genderId, creationTimestamp, modificationTimestamp);
    }
}