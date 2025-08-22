package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.pricing.CouponUsed;
import viteezy.domain.pricing.CouponUsedStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CouponUsedMapper implements RowMapper<CouponUsed> {

    @Override
    public CouponUsed map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long couponId = rs.getLong("coupon_id");
        final long customerId = rs.getLong("customer_id");
        final long paymentPlanId = rs.getLong("payment_plan_id");
        final CouponUsedStatus status = CouponUsedStatus.valueOf(rs.getString("status"));
        return new CouponUsed(id, couponId, customerId, paymentPlanId, status);
    }
}
