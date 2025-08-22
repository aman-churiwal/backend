package viteezy.service.blend;

import io.vavr.control.Try;
import org.springframework.cache.annotation.Cacheable;
import viteezy.domain.blend.BlendIngredientReason;
import viteezy.domain.blend.BlendIngredientReasonCode;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BlendIngredientReasonService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.BLEND_INGREDIENT_REASON, key = "'blendIngredientReasonId_'+#id")
    Try<Optional<BlendIngredientReason>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.BLEND_INGREDIENT_REASON, key = "'blendIngredientReasonCode_'+#code")
    Try<Optional<BlendIngredientReason>> find(BlendIngredientReasonCode code);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.BLEND_INGREDIENT_REASON, key = "'blendIngredientReason'")
    Try<List<BlendIngredientReason>> findAll();

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.BLEND_INGREDIENT_REASON, key = "'blendIngredientReasonMap'")
    Try<Map<BlendIngredientReasonCode, BlendIngredientReason>> findAllAsMap();

}
