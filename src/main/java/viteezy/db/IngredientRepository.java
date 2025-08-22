package viteezy.db;

import io.vavr.control.Try;
import viteezy.domain.ingredient.*;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository {

    Try<Ingredient> find(Long id);

    Try<List<Ingredient>> findAll();

    Try<List<IngredientComponent>> findAllComponents();

    Try<List<IngredientComponent>> findComponents(Long ingredientId);

    Try<Optional<IngredientContent>> findContent(Long ingredientId);

    Try<List<IngredientArticle>> findArticles(Long ingredientId);

    Try<Ingredient> save(Ingredient ingredient);

    Try<Ingredient> update(Ingredient ingredient);
}