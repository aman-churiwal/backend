package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.MenstruationMood;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface MenstruationMoodService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.MENSTRUATION_MOOD, key = "'menstruationMoodId_'+#id")
    Either<Throwable, Optional<MenstruationMood>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.MENSTRUATION_MOOD, key = "'menstruationMood'")
    Either<Throwable, List<MenstruationMood>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, MenstruationMood> save(MenstruationMood menstruationMood);

}