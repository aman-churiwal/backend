package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.HealthyLifestyle;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface HealthyLifestyleService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.HEALTHY_LIFESTYLE, key = "'healthyLifestyleId_'+#id")
    Either<Throwable, Optional<HealthyLifestyle>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.HEALTHY_LIFESTYLE, key = "'healthyLifestyle'")
    Either<Throwable, List<HealthyLifestyle>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, HealthyLifestyle> save(HealthyLifestyle healthyLifestyle);

}