package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.MenstruationInterval;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface MenstruationIntervalService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.MENSTRUATION_INTERVAL, key = "'menstruationIntervalId_'+#id")
    Either<Throwable, Optional<MenstruationInterval>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.MENSTRUATION_INTERVAL, key = "'menstruationInterval'")
    Either<Throwable, List<MenstruationInterval>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, MenstruationInterval> save(MenstruationInterval menstruationInterval);

}