package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.DigestionOccurrence;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface DigestionOccurrenceService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.DIGESTION_OCCURRENCE, key = "'digestionOccurrenceId_'+#id")
    Either<Throwable, Optional<DigestionOccurrence>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.DIGESTION_OCCURRENCE, key = "'digestionOccurrence'")
    Either<Throwable, List<DigestionOccurrence>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DigestionOccurrence> save(DigestionOccurrence digestionOccurrence);

}