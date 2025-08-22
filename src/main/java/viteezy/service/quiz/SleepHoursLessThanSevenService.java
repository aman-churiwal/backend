package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.SleepHoursLessThanSeven;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface SleepHoursLessThanSevenService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.SLEEP_HOURS_LESS_THAN_SEVEN, key = "'sleepHoursLessThanSevenId_'+#id")
    Either<Throwable, Optional<SleepHoursLessThanSeven>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.SLEEP_HOURS_LESS_THAN_SEVEN, key = "'sleepHoursLessThanSeven'")
    Either<Throwable, List<SleepHoursLessThanSeven>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SleepHoursLessThanSeven> save(SleepHoursLessThanSeven sleepHoursLessThanSeven);

}