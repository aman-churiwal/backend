package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ProductMapper implements RowMapper<Product> {

    @Override
    public Product map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String name = rs.getString("name");
        final String category = rs.getString("category");
        final String description = rs.getString("description");
        final String code = rs.getString("code");
        final String url = rs.getString("url");
        final boolean isVegan = rs.getBoolean("is_vegan");
        final boolean isActive = rs.getBoolean("is_active");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new Product(id, name, category, description, code, url, isVegan, isActive, creationTimestamp, modificationTimestamp);
    }
}