package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.pricing.Referral;
import viteezy.domain.pricing.ReferralStatus;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReferralMapper implements RowMapper<Referral> {

    @Override
    public Referral map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long from = rs.getLong("from_id");
        final long to = rs.getLong("to_id");
        final BigDecimal amount = rs.getBigDecimal("amount");
        final ReferralStatus status = ReferralStatus.valueOf(rs.getString("status"));
        final LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();
        final LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
        return new Referral(id, from, to, amount, status, creationDate, lastModified);
    }
}
