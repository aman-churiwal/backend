package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.DigestionAmount;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface DigestionAmountService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.DIGESTION_AMOUNT, key = "'digestionAmountId_'+#id")
    Either<Throwable, Optional<DigestionAmount>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.DIGESTION_AMOUNT, key = "'digestionAmount'")
    Either<Throwable, List<DigestionAmount>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DigestionAmount> save(DigestionAmount digestionAmount);

}