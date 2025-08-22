package viteezy.service;

import io.vavr.control.Try;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.controller.dto.IngredientGetResponse;
import viteezy.controller.dto.dashboard.IngredientPostRequest;
import viteezy.domain.ingredient.Ingredient;
import viteezy.service.cache.CacheNames;

import java.util.List;

public interface IngredientService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.INGREDIENTS, key = "'ingredientId_'+#id")
    Try<Ingredient> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.INGREDIENTS, key = "'ingredientId_content_'+#id")
    Try<IngredientGetResponse> findWithComponentsAndContent(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.INGREDIENTS, key = "'ingredient'")
    Try<List<Ingredient>> findAll();

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.INGREDIENTS, key = "'ingredient_components'")
    Try<List<IngredientGetResponse>> findAllWithComponents();

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.INGREDIENTS, key = "'additionalIngredient'")
    Try<List<Ingredient>> findAdditional();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Ingredient> save(Ingredient ingredient);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Ingredient> save(IngredientPostRequest ingredientPostRequest);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Try<Ingredient> update(IngredientPostRequest ingredientPostRequest);
}