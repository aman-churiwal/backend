package viteezy.db.jdbi.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.blend.BlendIngredient;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BlendIngredientMapper implements RowMapper<BlendIngredient> {

    @Override
    public BlendIngredient map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final long blendId = rs.getLong("blend_id");
        final long ingredientId = rs.getLong("ingredient_id");
        final BigDecimal amount = rs.getBigDecimal("amount");
        final String isUnit = rs.getString("is_unit");
        final BigDecimal price = rs.getBigDecimal("price");
        final String currency = rs.getString("currency");
        final String explanation = rs.getString("explanation");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new BlendIngredient(id, ingredientId, blendId, amount, isUnit, price, currency, explanation, creationTimestamp, modificationTimestamp);
    }
}