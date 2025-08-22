package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.OftenHavingFlu;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface OftenHavingFluService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.OFTEN_HAVING_FLUE, key = "'oftenHavingFluId_'+#id")
    Either<Throwable, Optional<OftenHavingFlu>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.OFTEN_HAVING_FLUE, key = "'oftenHavingFlu'")
    Either<Throwable, List<OftenHavingFlu>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, OftenHavingFlu> save(OftenHavingFlu oftenHavingFlu);

}