package viteezy.db.jdbi.mapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.ingredient.Ingredient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class IngredientMapper implements RowMapper<Ingredient> {

    @Override
    public Ingredient map(ResultSet rs, StatementContext ctx) throws SQLException {
        final long id = rs.getLong("id");
        final String name = rs.getString("name");
        final String type = rs.getString("type");
        final String description = rs.getString("description");
        final String code = rs.getString("code");
        final String url = rs.getString("url");
        final Integer strapiContentId = rs.getObject("strapi_content_id") != null ? rs.getInt("strapi_content_id") : null;
        final Boolean isAFlavour = rs.getBoolean("is_a_flavour");
        final Boolean isVegan = rs.getBoolean("is_vegan");
        final Integer priority = rs.getObject("priority") != null ? rs.getInt("priority") : null;
        final Boolean isActive = rs.getBoolean("is_active");
        final String sku = rs.getString("sku");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new Ingredient(id, name, type, description, code, url, strapiContentId, isAFlavour, isVegan, priority, isActive, sku, creationTimestamp, modificationTimestamp);
    }
}