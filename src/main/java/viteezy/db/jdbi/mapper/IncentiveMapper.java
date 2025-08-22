package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.pricing.Incentive;
import viteezy.domain.pricing.IncentiveType;
import viteezy.domain.pricing.IncentiveStatus;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class IncentiveMapper implements RowMapper<Incentive> {

    @Override
    public Incentive map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long customerId = rs.getLong("customer_id");
        final BigDecimal amount = rs.getBigDecimal("amount");
        final IncentiveStatus status = IncentiveStatus.valueOf(rs.getString("status"));
        final IncentiveType type = IncentiveType.valueOf(rs.getString("incentive_type"));
        final LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();
        final LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
        return new Incentive(id, customerId, amount, status, type, creationDate, lastModified);
    }
}
