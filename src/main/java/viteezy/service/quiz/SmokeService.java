package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.Smoke;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface SmokeService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.SMOKE, key = "'smokeId_'+#id")
    Either<Throwable, Optional<Smoke>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.SMOKE, key = "'smoke'")
    Either<Throwable, List<Smoke>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, Smoke> save(Smoke smoke);

}