package viteezy.db.jdbi.mapper.ingredient;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.ingredient.IngredientComponent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class IngredientComponentMapper implements RowMapper<IngredientComponent> {

    @Override
    public IngredientComponent map(ResultSet rs, StatementContext ctx) throws SQLException {
        final Long id = rs.getLong("id");
        final Long ingredientId = rs.getLong("ingredient_id");
        final String name = rs.getString("name");
        final String amount = rs.getString("amount");
        final String percentage = rs.getString("percentage");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new IngredientComponent(id, ingredientId, name, amount, percentage, creationTimestamp, modificationTimestamp);
    }
}