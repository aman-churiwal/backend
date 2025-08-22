package viteezy.db.jdbi.mapper;

import be.woutschoovaerts.mollie.data.payment.SequenceType;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.payment.Payment;
import viteezy.domain.payment.PaymentStatus;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class PaymentMapper implements RowMapper<Payment> {

    @Override
    public Payment map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final BigDecimal amount = rs.getBigDecimal("amount");
        final String molliePaymentId = rs.getString("mollie_payment_id");
        final String retriedMolliePaymentId = rs.getString("retried_mollie_payment_id");
        final long paymentPlanId = rs.getLong("payment_plan_id");
        final LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();
        final LocalDateTime paymentDate = Optional.ofNullable(rs.getTimestamp("payment_date")).map(Timestamp::toLocalDateTime).orElse(null);
        final LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
        final PaymentStatus status = PaymentStatus.valueOf(rs.getString("status"));
        final String reason = rs.getString("reason");
        final SequenceType sequenceType = SequenceType.valueOf(rs.getString("sequence_type").toUpperCase());
        return new Payment(id, amount, molliePaymentId, retriedMolliePaymentId, paymentPlanId, creationDate, paymentDate, lastModified, status, reason, sequenceType);
    }
}
