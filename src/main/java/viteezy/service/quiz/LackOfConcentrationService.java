package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.LackOfConcentration;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface LackOfConcentrationService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.LACK_OF_CONCENTRATION, key = "'lackOfConcentrationId_'+#id")
    Either<Throwable, Optional<LackOfConcentration>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.LACK_OF_CONCENTRATION, key = "'lackOfConcentration'")
    Either<Throwable, List<LackOfConcentration>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, LackOfConcentration> save(LackOfConcentration lackOfConcentration);

}