package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.payment.PaymentPlan;
import viteezy.domain.payment.PaymentPlanStatus;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class PaymentPlanMapper implements RowMapper<PaymentPlan> {

    @Override
    public PaymentPlan map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final BigDecimal firstAmount = rs.getBigDecimal("first_amount");
        final BigDecimal recurringAmount = rs.getBigDecimal("recurring_amount");
        final int recurringMonths = rs.getInt("recurring_months");
        final long customerId = rs.getLong("customer_id");
        final long blendId = rs.getLong("blend_id");
        final UUID externalReference = getExternalReference(rs);
        final LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();
        final LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
        final String rawStatus = rs.getString("status");
        final PaymentPlanStatus status = PaymentPlanStatus.valueOf(rawStatus);
        final LocalDateTime paymentDate = rs.getTimestamp("payment_date").toLocalDateTime();
        final String stopReason = rs.getString("stop_reason");
        final LocalDateTime deliveryDate = rs.getTimestamp("delivery_date").toLocalDateTime();
        final Optional<LocalDateTime> nextPaymentDate = Optional.ofNullable(rs.getTimestamp("next_payment_date")).map(Timestamp::toLocalDateTime);
        final Optional<LocalDateTime> nextDeliveryDate = Optional.ofNullable(rs.getTimestamp("next_delivery_date")).map(Timestamp::toLocalDateTime);
        final String paymentMethod = rs.getString("payment_method");
        return new PaymentPlan(id, firstAmount, recurringAmount, recurringMonths, customerId,
                blendId, externalReference, creationDate, lastModified, status, paymentDate, stopReason, deliveryDate,
                nextPaymentDate, nextDeliveryDate, paymentMethod);
    }

    private UUID getExternalReference(ResultSet rs) throws SQLException {
        String rawExternalReference = rs.getString("external_reference");
        return UUID.fromString(rawExternalReference);
    }
}
