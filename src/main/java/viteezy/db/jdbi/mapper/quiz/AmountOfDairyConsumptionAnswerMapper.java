package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.AmountOfDairyConsumptionAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AmountOfDairyConsumptionAnswerMapper implements RowMapper<AmountOfDairyConsumptionAnswer> {

    @Override
    public AmountOfDairyConsumptionAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long amountOfDairyConsumptionId = rs.getLong("amount_of_dairy_consumption_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new AmountOfDairyConsumptionAnswer(id, quizId, amountOfDairyConsumptionId, creationTimestamp, modificationTimestamp);
    }
}