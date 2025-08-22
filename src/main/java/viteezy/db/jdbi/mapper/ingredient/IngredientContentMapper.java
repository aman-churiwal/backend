package viteezy.db.jdbi.mapper.ingredient;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.ingredient.IngredientContent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class IngredientContentMapper implements RowMapper<IngredientContent> {

    @Override
    public IngredientContent map(ResultSet rs, StatementContext ctx) throws SQLException {
        final Long id = rs.getLong("id");
        final Long ingredientId = rs.getLong("ingredient_id");
        final String description = rs.getString("description");
        final String bullets = rs.getString("bullets");
        final String title1 = rs.getString("title_1");
        final String text1 = rs.getString("text_1");
        final String title2 = rs.getString("title_2");
        final String text2 = rs.getString("text_2");
        final String title3 = rs.getString("title_3");
        final String text3 = rs.getString("text_3");
        final String notice = rs.getString("description");
        final String excipients = rs.getString("excipients");
        final String claim = rs.getString("claim");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new IngredientContent(id, ingredientId, description, bullets, title1, text1, title2, text2, title3, text3, notice, null, excipients, claim, creationTimestamp, modificationTimestamp);
    }
}