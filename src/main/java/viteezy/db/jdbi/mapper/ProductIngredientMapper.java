package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.ProductIngredient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ProductIngredientMapper implements RowMapper<ProductIngredient> {

    @Override
    public ProductIngredient map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final Long productId = rs.getLong("product_id");
        final Long ingredientId = rs.getLong("ingredient_id");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new ProductIngredient(id, productId, ingredientId, creationTimestamp, modificationTimestamp);
    }
}