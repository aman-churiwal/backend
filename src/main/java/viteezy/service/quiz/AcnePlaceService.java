package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.AcnePlace;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface AcnePlaceService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.ACNE_PLACE, key = "'acnePlaceId_'+#id")
    Either<Throwable, Optional<AcnePlace>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.ACNE_PLACE, key = "'acnePlace'")
    Either<Throwable, List<AcnePlace>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AcnePlace> save(AcnePlace acnePlace);

}