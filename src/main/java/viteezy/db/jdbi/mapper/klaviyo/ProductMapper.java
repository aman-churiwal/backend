package viteezy.db.jdbi.mapper.klaviyo;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.klaviyo.Product;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {

    @Override
    public Product map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String name = rs.getString("name");
        final String description = rs.getString("description");
        final String code = rs.getString("code");
        final String url = rs.getString("url");
        final BigDecimal price = rs.getBigDecimal("price");
        return new Product(id, name, description, code, url, price);
    }
}