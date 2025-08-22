package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.Review;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReviewMapper implements RowMapper<Review>  {

    @Override
    public Review map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String source = rs.getString("source");
        final Integer total = rs.getInt("total");
        final Integer minScore = rs.getInt("min_score");
        final Integer maxScore = rs.getInt("max_score");
        final BigDecimal score = rs.getBigDecimal("score");
        final String scoreLabel = rs.getString("score_label");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new Review(id, source, total, minScore, maxScore, score, scoreLabel, creationTimestamp, modificationTimestamp);
    }
}