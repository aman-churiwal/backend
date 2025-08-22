package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.AverageSleepingHours;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface AverageSleepingHoursService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.AVERAGE_SLEEPING_HOURS, key = "'averageSleepingHoursId_'+#id")
    Either<Throwable, Optional<AverageSleepingHours>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.AVERAGE_SLEEPING_HOURS, key = "'averageSleepingHours'")
    Either<Throwable, List<AverageSleepingHours>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AverageSleepingHours> save(AverageSleepingHours averageSleepingHours);

}