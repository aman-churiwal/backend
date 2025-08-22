package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.BingeEating;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface BingeEatingService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.BINGE_EATING, key = "'bingeEatingId_'+#id")
    Either<Throwable, Optional<BingeEating>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.BINGE_EATING, key = "'bingeEating'")
    Either<Throwable, List<BingeEating>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, BingeEating> save(BingeEating bingeEating);

}