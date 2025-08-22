package viteezy.db;

import io.vavr.control.Try;
import viteezy.domain.ingredient.IngredientPrice;

import java.util.List;

public interface IngredientPriceRepository {

    Try<IngredientPrice> find(Long id);

    Try<IngredientPrice> findActiveByIngredientId(Long ingredientId);

    Try<List<IngredientPrice>> findAll();

    Try<List<IngredientPrice>> findAllActive();

    Try<IngredientPrice> save(IngredientPrice ingredientPrice);
}