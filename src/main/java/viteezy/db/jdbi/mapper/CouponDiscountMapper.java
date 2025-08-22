package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.pricing.CouponDiscount;
import viteezy.domain.pricing.CouponDiscountStatus;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CouponDiscountMapper implements RowMapper<CouponDiscount> {

    @Override
    public CouponDiscount map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long couponId = rs.getLong("coupon_id");
        final long paymentPlanId = rs.getLong("payment_plan_id");
        final Integer month = rs.getInt("month");
        final BigDecimal amount = rs.getBigDecimal("amount");
        final CouponDiscountStatus status = CouponDiscountStatus.valueOf(rs.getString("status"));
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new CouponDiscount(id, couponId, paymentPlanId, month, amount, status, creationTimestamp, modificationTimestamp);
    }
}
