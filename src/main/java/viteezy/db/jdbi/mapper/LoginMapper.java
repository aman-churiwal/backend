package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.Login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LoginMapper implements RowMapper<Login>  {

    @Override
    public Login map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String email = rs.getString("email");
        final String token = rs.getString("token");
        final LocalDateTime lastUpdated = rs.getTimestamp("last_updated").toLocalDateTime();
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        return new Login(id, email, token, lastUpdated, creationTimestamp);
    }
}