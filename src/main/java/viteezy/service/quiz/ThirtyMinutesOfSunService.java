package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.ThirtyMinutesOfSun;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface ThirtyMinutesOfSunService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.THIRTY_MINUTES_OF_SUN, key = "'thirtyMinutesOfSunId_'+#id")
    Either<Throwable, Optional<ThirtyMinutesOfSun>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.THIRTY_MINUTES_OF_SUN, key = "'thirtyMinutesOfSun'")
    Either<Throwable, List<ThirtyMinutesOfSun>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, ThirtyMinutesOfSun> save(ThirtyMinutesOfSun thirtyMinutesOfSun);

}