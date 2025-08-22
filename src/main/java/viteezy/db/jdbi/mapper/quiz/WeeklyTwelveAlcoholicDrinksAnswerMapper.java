package viteezy.db.jdbi.mapper.quiz;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.quiz.WeeklyTwelveAlcoholicDrinksAnswer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class WeeklyTwelveAlcoholicDrinksAnswerMapper implements RowMapper<WeeklyTwelveAlcoholicDrinksAnswer> {

    @Override
    public WeeklyTwelveAlcoholicDrinksAnswer map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long quizId = rs.getLong("quiz_id");
        final long weeklyTwelveAlcoholicDrinksId = rs.getLong("weekly_twelve_alcoholic_drinks_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new WeeklyTwelveAlcoholicDrinksAnswer(id, quizId, weeklyTwelveAlcoholicDrinksId, creationTimestamp, modificationTimestamp);
    }
}