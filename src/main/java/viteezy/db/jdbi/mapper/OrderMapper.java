package viteezy.db.jdbi.mapper;

import be.woutschoovaerts.mollie.data.payment.SequenceType;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.fulfilment.Order;
import viteezy.domain.fulfilment.OrderStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderMapper implements RowMapper<Order>  {

    @Override
    public Order map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final UUID externalReference = UUID.fromString(rs.getString("external_reference"));
        final String orderNumber = rs.getString("order_number");
        final long paymentId = rs.getLong("payment_id");
        final SequenceType sequenceType = SequenceType.valueOf(rs.getString("sequence_type").toUpperCase());
        final long paymentPlanId = rs.getLong("payment_plan_id");
        final long blendId = rs.getLong("blend_id");
        final long customerId = rs.getLong("customer_id");
        final int recurringMonths = rs.getInt("recurring_months");
        final String shipToFirstName = rs.getString("first_name");
        final String shipToLastName = rs.getString("last_name");
        final String shipToStreet = rs.getString("street");
        final String shipToHouseNo = rs.getString("house_number");
        final String shipToAnnex = rs.getString("house_number_addition");
        final String shipToPostalCode = rs.getString("postcode");
        final String shipToCity = rs.getString("city");
        final String shipToCountryCode = rs.getString("country");
        final String shipToPhone = rs.getString("phone_number");
        final String shipToEmail = rs.getString("email");
        final String referralCode = rs.getString("referral_code");
        final String trackTraceCode = rs.getString("tracktrace");
        final String pharmacistOrderNumber = rs.getString("pharmacist_order_number");
        final OrderStatus status = OrderStatus.valueOf(rs.getString("status"));
        final LocalDateTime created = rs.getTimestamp("created").toLocalDateTime();
        final LocalDateTime shipped = getNullableTimestamp(rs, "shipped");
        final LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
        return new Order(id, externalReference, orderNumber, paymentId, sequenceType, paymentPlanId, blendId, customerId,
                recurringMonths, shipToFirstName, shipToLastName, shipToStreet, shipToHouseNo, shipToAnnex,
                shipToPostalCode, shipToCity, shipToCountryCode, shipToPhone, shipToEmail, referralCode, trackTraceCode,
                pharmacistOrderNumber, status, created, shipped, lastModified);
    }

    private LocalDateTime getNullableTimestamp(ResultSet rs, String columnLabel) throws SQLException {
        final Timestamp timestamp = rs.getTimestamp(columnLabel);
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}