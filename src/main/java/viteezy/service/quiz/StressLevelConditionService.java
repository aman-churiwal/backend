package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.StressLevelCondition;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface StressLevelConditionService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.STRESS_LEVEL_CONDITION, key = "'stressLevelConditionId_'+#id")
    Either<Throwable, Optional<StressLevelCondition>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.STRESS_LEVEL_CONDITION, key = "'stressLevelCondition'")
    Either<Throwable, List<StressLevelCondition>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, StressLevelCondition> save(StressLevelCondition stressLevelCondition);

}