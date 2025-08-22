package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.StressLevel;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface StressLevelService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.STRESS_LEVEL, key = "'stressLevelId_'+#id")
    Either<Throwable, Optional<StressLevel>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.STRESS_LEVEL, key = "'stressLevel'")
    Either<Throwable, List<StressLevel>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, StressLevel> save(StressLevel stressLevel);

}