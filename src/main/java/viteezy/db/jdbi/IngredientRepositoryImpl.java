package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.IngredientRepository;
import viteezy.domain.ingredient.*;
import viteezy.domain.payment.PaymentPlan;

import java.util.List;
import java.util.Optional;

public class IngredientRepositoryImpl implements IngredientRepository {

    private static final String SELECT_ALL = "SELECT ingredients.* FROM ingredients ";

    private static final String SELECT_ALL_COMPONENTS = "SELECT ingredient_components.* FROM ingredient_components ";

    private static final String SELECT_ALL_CONTENT = "SELECT ingredient_content.* FROM ingredient_content ";

    private static final String SELECT_ALL_ARTICLES = "SELECT ingredient_articles.* FROM ingredient_articles ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO ingredients (name, type, description, code, url, strapi_content_id, is_a_flavour, is_vegan, priority, is_active, sku) " +
            "VALUES (:name, :type, :description, :code, :url, :strapiContentId, :isAFlavour, :isVegan, :priority, :isActive, :sku)";

    private static final String UPDATE_QUERY = "" +
            "UPDATE ingredients " +
            "SET name = :name, type = :type, description = :description, code = :code, url = :url, " +
            "strapi_content_id = :strapiContentId, is_a_flavour = :isAFlavour, is_vegan = :isVegan, priority = :priority, " +
            "is_active = :isActive, sku = :sku, modification_timestamp = NOW() " +
            "WHERE id = :id";

    private final Jdbi jdbi;

    public IngredientRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<Ingredient> find(Long id) {
        final HandleCallback<Ingredient, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(Ingredient.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<Ingredient>> findAll() {
        final HandleCallback<List<Ingredient>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(Ingredient.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<IngredientComponent>> findAllComponents() {
        final HandleCallback<List<IngredientComponent>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_COMPONENTS)
                .mapTo(IngredientComponent.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<IngredientComponent>> findComponents(Long ingredientId) {
        final HandleCallback<List<IngredientComponent>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_COMPONENTS + "WHERE ingredient_id = :ingredientId")
                .bind("ingredientId", ingredientId)
                .mapTo(IngredientComponent.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Optional<IngredientContent>> findContent(Long ingredientId) {
        final HandleCallback<Optional<IngredientContent>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_CONTENT + "WHERE ingredient_id = :ingredientId")
                .bind("ingredientId", ingredientId)
                .mapTo(IngredientContent.class)
                .findFirst();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<IngredientArticle>> findArticles(Long ingredientId) {
        final HandleCallback<List<IngredientArticle>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL_ARTICLES + "WHERE ingredient_id = :ingredientId")
                .bind("ingredientId", ingredientId)
                .mapTo(IngredientArticle.class)
                .list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<Ingredient> save(Ingredient ingredient) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(ingredient)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }

    @Override
    public Try<Ingredient> update(Ingredient ingredient) {
        final HandleCallback<Integer, RuntimeException> queryCallback = handle -> handle
                .createUpdate(UPDATE_QUERY)
                .bindBean(ingredient)
                .execute();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(integer -> find(ingredient.getId()));
    }
}