package viteezy.db.jdbi.mapper.ingredient;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.ingredient.IngredientPrice;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientPriceMapper implements RowMapper<IngredientPrice> {

    @Override
    public IngredientPrice map(ResultSet rs, StatementContext ctx) throws SQLException {
        final Long id = rs.getLong("id");
        final Long ingredientId = rs.getLong("ingredient_id");
        final BigDecimal amount = rs.getBigDecimal("amount");
        final String internationalSystemUnit = rs.getString("international_system_unit");
        final BigDecimal price = rs.getBigDecimal("price");
        final String currency = rs.getString("currency");
        return new IngredientPrice(id, ingredientId, amount, internationalSystemUnit, price, currency);
    }
}