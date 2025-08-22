package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.IngredientPriceRepository;
import viteezy.domain.ingredient.IngredientPrice;

import java.util.List;

public class IngredientPriceRepositoryImpl implements IngredientPriceRepository {

    private static final String SELECT_ALL = "SELECT ingredient_prices.* FROM ingredient_prices ";

    private static final String INSERT_QUERY = "" +
            "INSERT INTO ingredient_prices (ingredient_id, amount, international_system_unit, price, currency) " +
            "VALUES (:ingredientId, :amount, :internationalSystemUnit, :price, :currency)";

    private final Jdbi jdbi;

    public IngredientPriceRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<IngredientPrice> find(Long id) {
        final HandleCallback<IngredientPrice, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(IngredientPrice.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<IngredientPrice> findActiveByIngredientId(Long ingredientId) {
        final HandleCallback<IngredientPrice, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE ingredient_id = :ingredientId AND international_system_unit != 'UNITLESS'")
                .bind("ingredientId", ingredientId)
                .mapTo(IngredientPrice.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<IngredientPrice>> findAll() {
        final HandleCallback<List<IngredientPrice>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(IngredientPrice.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<IngredientPrice>> findAllActive() {
        final HandleCallback<List<IngredientPrice>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "JOIN ingredients ON ingredients.id=ingredient_prices.ingredient_id WHERE international_system_unit != 'UNITLESS' AND is_active=1")
                .mapTo(IngredientPrice.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<IngredientPrice> save(IngredientPrice ingredientPrice) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(ingredientPrice)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }
}
