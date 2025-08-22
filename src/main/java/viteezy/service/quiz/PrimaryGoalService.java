package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.PrimaryGoal;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface PrimaryGoalService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.PRIMARY_GOAL, key = "'primaryGoalId_'+#id")
    Either<Throwable, Optional<PrimaryGoal>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.PRIMARY_GOAL, key = "'primaryGoal'")
    Either<Throwable, List<PrimaryGoal>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, PrimaryGoal> save(PrimaryGoal primaryGoal);

}