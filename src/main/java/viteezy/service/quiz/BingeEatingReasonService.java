package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.BingeEatingReason;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface BingeEatingReasonService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.BINGE_EATING_REASON, key = "'bingeEatingReasonId_'+#id")
    Either<Throwable, Optional<BingeEatingReason>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.BINGE_EATING_REASON, key = "'bingeEatingReason'")
    Either<Throwable, List<BingeEatingReason>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, BingeEatingReason> save(BingeEatingReason bingeEatingReason);

}