package viteezy.db.jdbi.mapper.dashboard;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.dashboard.AuthToken;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AuthTokenMapper implements RowMapper<AuthToken> {
    @Override
    public AuthToken map(ResultSet rs, StatementContext ctx) throws SQLException {
        final Long id = rs.getLong("id");
        final String token = rs.getString("token");
        final long userId = rs.getLong("user_id");
        final String userRole = rs.getString("user_role");
        final LocalDateTime lastAccess = rs.getTimestamp("last_access").toLocalDateTime();
        return new AuthToken(id, token, userId, userRole, lastAccess);
    }
}