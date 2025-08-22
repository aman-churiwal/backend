package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.NewProductAvailableAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class NewProductAvailableAnswerMapper implements RowMapper<NewProductAvailableAnswer> {

    @Override
    public NewProductAvailableAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long newProductAvailableId = rs.getLong("new_product_available_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new NewProductAvailableAnswer(id, quizId, newProductAvailableId, creationTimestamp, modificationTimestamp);
    }
}