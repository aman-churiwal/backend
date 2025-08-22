package viteezy.service;

import io.vavr.control.Try;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.ingredient.IngredientUnit;
import viteezy.service.cache.CacheNames;

import java.util.List;

public interface IngredientUnitService {

    @Cacheable(cacheManager = "hourLivedCacheManager", cacheNames = CacheNames.INGREDIENTS, key = "'ingredient_units'")
    Try<List<IngredientUnit>> findAllUnits();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<IngredientUnit> save(IngredientUnit ingredientUnit);
}