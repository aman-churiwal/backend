package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.TypeOfTrainingAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TypeOfTrainingAnswerMapper implements RowMapper<TypeOfTrainingAnswer> {

    @Override
    public TypeOfTrainingAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long typeOfTrainingId = rs.getLong("type_of_training_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new TypeOfTrainingAnswer(id, quizId, typeOfTrainingId, creationTimestamp, modificationTimestamp);
    }
}