package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.ConfigurationDatabaseObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FacebookAccessTokenMapper implements RowMapper<ConfigurationDatabaseObject> {

    @Override
    public ConfigurationDatabaseObject map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String name = rs.getString("name");
        final String type = rs.getString("type");
        final String value = rs.getString("value");
        final LocalDateTime expirationTimestamp = rs.getTimestamp("expiration_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        return new ConfigurationDatabaseObject(id, name, type, value, expirationTimestamp, modificationTimestamp, creationTimestamp);
    }
}
