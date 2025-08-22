package viteezy.db.jdbi;

import io.vavr.control.Try;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.Jdbi;
import viteezy.db.IngredientUnitRepository;
import viteezy.domain.ingredient.IngredientUnit;

import java.util.List;

public class IngredientUnitRepositoryImpl implements IngredientUnitRepository {

    private static final String SELECT_ALL = "SELECT ingredient_units.* FROM ingredient_units ";
    private static final String INSERT_QUERY = "" +
            "INSERT INTO ingredient_units (ingredient_id, pharmacist_code, pharmacist_size, pharmacist_unit) " +
            "VALUES (:ingredientId, :pharmacistCode, :pharmacistSize, :pharmacistUnit)";

    private final Jdbi jdbi;

    public IngredientUnitRepositoryImpl(Jdbi dbi) {
        this.jdbi = dbi;
    }

    @Override
    public Try<IngredientUnit> find(Long id) {
        final HandleCallback<IngredientUnit, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL + "WHERE id = :id")
                .bind("id", id)
                .mapTo(IngredientUnit.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<List<IngredientUnit>> findAllUnits() {
        final HandleCallback<List<IngredientUnit>, RuntimeException> queryCallback = handle -> handle
                .createQuery(SELECT_ALL)
                .mapTo(IngredientUnit.class).list();
        return Try.of(() -> jdbi.withHandle(queryCallback));
    }

    @Override
    public Try<IngredientUnit> save(IngredientUnit ingredientUnit) {
        final HandleCallback<Long, RuntimeException> queryCallback = handle -> handle
                .createUpdate(INSERT_QUERY)
                .bindBean(ingredientUnit)
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).one();
        return Try.of(() -> jdbi.withHandle(queryCallback))
                .flatMap(this::find);
    }
}