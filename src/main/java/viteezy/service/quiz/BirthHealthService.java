package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.BirthHealth;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface BirthHealthService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.BIRTH_HEALTH, key = "'birthHealthId_'+#id")
    Either<Throwable, Optional<BirthHealth>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.BIRTH_HEALTH, key = "'birthHealth'")
    Either<Throwable, List<BirthHealth>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, BirthHealth> save(BirthHealth birthHealth);

}