package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.fulfilment.PharmacistOrder;
import viteezy.domain.fulfilment.PharmacistOrderStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PharmacistOrderMapper implements RowMapper<PharmacistOrder>  {

    @Override
    public PharmacistOrder map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String batchName = rs.getString("batch_name");
        final Integer batchNumber = rs.getInt("batch_number");
        final String orderNumber = rs.getString("order_number");
        final String fileName = rs.getString("file_name");
        final PharmacistOrderStatus status = PharmacistOrderStatus.valueOf(rs.getString("status"));
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new PharmacistOrder(id, batchName, batchNumber, orderNumber, fileName, status, creationTimestamp, modificationTimestamp);
    }
}