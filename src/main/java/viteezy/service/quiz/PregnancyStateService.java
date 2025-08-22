package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.PregnancyState;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface PregnancyStateService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.PREGNANCY_STATE, key = "'pregnancyStateId_'+#id")
    Either<Throwable, Optional<PregnancyState>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.PREGNANCY_STATE, key = "'pregnancyState'")
    Either<Throwable, List<PregnancyState>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, PregnancyState> save(PregnancyState pregnancyState);

}