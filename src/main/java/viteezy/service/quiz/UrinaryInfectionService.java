package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.UrinaryInfection;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface UrinaryInfectionService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.URINARY_INFECTION, key = "'urinaryInfectionId_'+#id")
    Either<Throwable, Optional<UrinaryInfection>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.URINARY_INFECTION, key = "'urinaryInfection'")
    Either<Throwable, List<UrinaryInfection>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, UrinaryInfection> save(UrinaryInfection urinaryInfection);

}