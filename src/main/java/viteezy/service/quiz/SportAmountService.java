package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.SportAmount;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface SportAmountService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.SPORT_AMOUNT, key = "'sportAmountId_'+#id")
    Either<Throwable, Optional<SportAmount>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.SPORT_AMOUNT, key = "'sportAmount'")
    Either<Throwable, List<SportAmount>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SportAmount> save(SportAmount sportAmount);

}