package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.SportReason;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface SportReasonService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.SPORT_REASON, key = "'sportReasonId_'+#id")
    Either<Throwable, Optional<SportReason>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.SPORT_REASON, key = "'sportReason'")
    Either<Throwable, List<SportReason>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SportReason> save(SportReason sportReason);

}