package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.AmountOfVegetableConsumptionAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AmountOfVegetableConsumptionAnswerMapper implements RowMapper<AmountOfVegetableConsumptionAnswer> {

    @Override
    public AmountOfVegetableConsumptionAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long amountOfVegetableConsumptionId = rs.getLong("amount_of_vegetable_consumption_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new AmountOfVegetableConsumptionAnswer(id, quizId, amountOfVegetableConsumptionId, creationTimestamp, modificationTimestamp);
    }
}