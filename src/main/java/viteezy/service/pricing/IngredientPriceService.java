package viteezy.service.pricing;

import io.vavr.control.Try;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.ingredient.IngredientPrice;
import viteezy.service.cache.CacheNames;

import java.util.List;

public interface IngredientPriceService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.INGREDIENT_PRICES, key = "'ingredientId_'+#id")
    Try<IngredientPrice> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.INGREDIENT_PRICES, key = "'ingredientId_'+#ingredientId")
    Try<IngredientPrice> findActiveByIngredientId(Long ingredientId);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.INGREDIENT_PRICES, key = "'active'")
    Try<List<IngredientPrice>> findAllActive();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<IngredientPrice> save(IngredientPrice ingredientPrice);

}