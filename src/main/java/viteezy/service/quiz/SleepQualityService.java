package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.SleepQuality;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface SleepQualityService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.SLEEP_QUALITY, key = "'sleepQualityId_'+#id")
    Either<Throwable, Optional<SleepQuality>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.SLEEP_QUALITY, key = "'sleepQuality'")
    Either<Throwable, List<SleepQuality>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SleepQuality> save(SleepQuality sleepQuality);

}