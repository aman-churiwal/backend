package viteezy.db.jdbi.mapper;

import io.vavr.control.Try;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.pricing.Coupon;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class CouponMapper implements RowMapper<Coupon> {

    @Override
    public Coupon map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String couponCode = rs.getString("coupon_code");
        final LocalDateTime startDate = rs.getTimestamp("start_date").toLocalDateTime();
        final LocalDateTime endDate = rs.getTimestamp("end_date").toLocalDateTime();
        final BigDecimal amount = rs.getBigDecimal("amount");
        final BigDecimal minimumAmount = rs.getBigDecimal("minimum_amount");
        final BigDecimal maximumAmount = rs.getBigDecimal("maximum_amount");
        final Boolean percentage = rs.getBoolean("percentage");
        final Integer maxUses = rs.getInt("max_uses");
        final Integer used = rs.getInt("used");
        final Optional<Integer> recurringMonths = getNullableInteger(rs);
        final Optional<String> recurringTerms = getNullableString(rs);
        final Boolean isRecurring = rs.getBoolean("is_recurring");
        final Optional<Long> ingredientId = getNullableLong(rs);
        final LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();
        return new Coupon(id, couponCode, startDate, endDate, amount, minimumAmount, maximumAmount, percentage, maxUses, used, recurringMonths, recurringTerms, isRecurring, ingredientId, creationDate);
    }

    private Optional<String> getNullableString(ResultSet rs) throws SQLException {
        return Try.success(rs.getString("recurring_terms"))
                .filterTry(__ -> !rs.wasNull())
                .toJavaOptional();
    }

    private Optional<Integer> getNullableInteger(ResultSet rs) throws SQLException {
        return Try.success(rs.getInt("recurring_months"))
                .filterTry(__ -> !rs.wasNull())
                .toJavaOptional();
    }

    private Optional<Long> getNullableLong(ResultSet rs) throws SQLException {
        return Try.success(rs.getLong("ingredient_id"))
                .filterTry(__ -> !rs.wasNull())
                .toJavaOptional();
    }
}
