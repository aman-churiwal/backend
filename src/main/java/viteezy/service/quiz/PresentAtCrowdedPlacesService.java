package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.PresentAtCrowdedPlaces;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface PresentAtCrowdedPlacesService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.PRESENT_AT_CROWDED_PLACES, key = "'presentAtCrowdedPlacesId_'+#id")
    Either<Throwable, Optional<PresentAtCrowdedPlaces>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.PRESENT_AT_CROWDED_PLACES, key = "'presentAtCrowdedPlaces'")
    Either<Throwable, List<PresentAtCrowdedPlaces>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, PresentAtCrowdedPlaces> save(PresentAtCrowdedPlaces presentAtCrowdedPlaces);

}