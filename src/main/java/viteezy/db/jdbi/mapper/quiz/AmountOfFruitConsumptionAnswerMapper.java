package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.AmountOfFruitConsumptionAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AmountOfFruitConsumptionAnswerMapper implements RowMapper<AmountOfFruitConsumptionAnswer> {

    @Override
    public AmountOfFruitConsumptionAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long amountOfFruitConsumptionId = rs.getLong("amount_of_fruit_consumption_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new AmountOfFruitConsumptionAnswer(id, quizId, amountOfFruitConsumptionId, creationTimestamp, modificationTimestamp);
    }
}