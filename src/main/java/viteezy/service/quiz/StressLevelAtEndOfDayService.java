package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.StressLevelAtEndOfDay;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface StressLevelAtEndOfDayService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.STRESS_LEVEL_AT_END_OF_DAY, key = "'stressLevelAtEndOfDayId_'+#id")
    Either<Throwable, Optional<StressLevelAtEndOfDay>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.STRESS_LEVEL_AT_END_OF_DAY, key = "'stressLevelAtEndOfDay'")
    Either<Throwable, List<StressLevelAtEndOfDay>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, StressLevelAtEndOfDay> save(StressLevelAtEndOfDay stressLevelAtEndOfDay);

}