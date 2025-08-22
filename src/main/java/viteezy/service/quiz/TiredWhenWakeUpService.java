package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.TiredWhenWakeUp;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface TiredWhenWakeUpService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.TIRED_WHEN_WAKE_UP, key = "'tiredWhenWakeUpId_'+#id")
    Either<Throwable, Optional<TiredWhenWakeUp>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.TIRED_WHEN_WAKE_UP, key = "'tiredWhenWakeUp'")
    Either<Throwable, List<TiredWhenWakeUp>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, TiredWhenWakeUp> save(TiredWhenWakeUp tiredWhenWakeUp);

}