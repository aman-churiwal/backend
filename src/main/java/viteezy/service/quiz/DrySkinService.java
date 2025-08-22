package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.DrySkin;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface DrySkinService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.DRY_SKIN, key = "'drySkinId_'+#id")
    Either<Throwable, Optional<DrySkin>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.DRY_SKIN, key = "'drySkin'")
    Either<Throwable, List<DrySkin>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DrySkin> save(DrySkin drySkin);

}