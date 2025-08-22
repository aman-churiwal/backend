package viteezy.db.jdbi.mapper.ingredient;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import viteezy.domain.ingredient.IngredientArticle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class IngredientArticleMapper implements RowMapper<IngredientArticle> {

    @Override
    public IngredientArticle map(ResultSet rs, StatementContext ctx) throws SQLException {
        final Long id = rs.getLong("id");
        final Long ingredientId = rs.getLong("ingredient_id");
        final String author = rs.getString("author");
        final String title = rs.getString("title");
        final String url = rs.getString("url");
        final String source = rs.getString("source");
        final LocalDateTime creationTimestamp = rs.getTimestamp("creation_timestamp").toLocalDateTime();
        final LocalDateTime modificationTimestamp = rs.getTimestamp("modification_timestamp").toLocalDateTime();
        return new IngredientArticle(id, ingredientId, author, title, url, source, creationTimestamp, modificationTimestamp);
    }
}