package viteezy.db.jdbi.mapper.ingredient;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.ingredient.IngredientUnit;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class IngredientUnitMapper implements RowMapper<IngredientUnit> {

    @Override
    public IngredientUnit map(ResultSet rs, StatementContext ctx) throws SQLException {
        final Long id = rs.getLong("id");
        final Long ingredientId = rs.getLong("ingredient_id");
        final Long pharmacistCode = rs.getLong("pharmacist_code");
        final String pharmacistSize = rs.getString("pharmacist_size");
        final BigDecimal pharmacistUnit = rs.getBigDecimal("pharmacist_unit");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new IngredientUnit(id, ingredientId, pharmacistCode, pharmacistSize, pharmacistUnit, creationTimestamp, modificationTimestamp);
    }
}