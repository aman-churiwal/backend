package viteezy.db.jdbi.mapper.dashboard;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.dashboard.User;
import viteezy.domain.dashboard.UserRoles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserMapper implements RowMapper<User> {
    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String email = rs.getString("email");
        final String password = rs.getString("password");
        final String firstName = rs.getString("first_name");
        final String lastName = rs.getString("last_name");
        final LocalDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime();
        final UserRoles role = UserRoles.valueOf(rs.getString("role"));

        return new User(id, email, password, firstName, lastName, creationDate, role);
    }
}
