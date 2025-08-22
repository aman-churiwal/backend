package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.TroubleFallingAsleep;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface TroubleFallingAsleepService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.TROUBLE_FALLING_ASLEEP, key = "'troubleFallingAsleepId_'+#id")
    Either<Throwable, Optional<TroubleFallingAsleep>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.TROUBLE_FALLING_ASLEEP, key = "'troubleFallingAsleep'")
    Either<Throwable, List<TroubleFallingAsleep>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, TroubleFallingAsleep> save(TroubleFallingAsleep troubleFallingAsleep);

}