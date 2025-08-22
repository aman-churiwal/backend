package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.UsageGoal;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface UsageGoalService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.USAGE_GOAL, key = "'usageGoalId_'+#id")
    Either<Throwable, Optional<UsageGoal>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.USAGE_GOAL, key = "'usageGoal'")
    Either<Throwable, List<UsageGoal>> findAll();

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.USAGE_GOAL, key = "'usageGoal_active'")
    Either<Throwable, List<UsageGoal>> findActive();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, UsageGoal> save(UsageGoal usageGoal);

}