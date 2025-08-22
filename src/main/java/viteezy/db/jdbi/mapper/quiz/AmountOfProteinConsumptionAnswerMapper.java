package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.AmountOfProteinConsumptionAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AmountOfProteinConsumptionAnswerMapper implements RowMapper<AmountOfProteinConsumptionAnswer> {

    @Override
    public AmountOfProteinConsumptionAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long amountOfProteinConsumptionId = rs.getLong("amount_of_protein_consumption_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new AmountOfProteinConsumptionAnswer(id, quizId, amountOfProteinConsumptionId, creationTimestamp, modificationTimestamp);
    }
}