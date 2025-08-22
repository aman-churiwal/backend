package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.WebsiteContent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class WebsiteContentMapper implements RowMapper<WebsiteContent> {

    @Override
    public WebsiteContent map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String code = rs.getString("code");
        final String title = rs.getString("title");
        final String subtitle = rs.getString("subtitle");
        final Boolean isActive = rs.getBoolean("is_active");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new WebsiteContent(id, code, title, subtitle, isActive, creationTimestamp, modificationTimestamp);
    }
}