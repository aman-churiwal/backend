package viteezy.db;

import io.vavr.control.Try;
import viteezy.domain.ingredient.IngredientUnit;

import java.util.List;

public interface IngredientUnitRepository {

    Try<IngredientUnit> find(Long id);

    Try<List<IngredientUnit>> findAllUnits();

    Try<IngredientUnit> save(IngredientUnit ingredientUnit);
}