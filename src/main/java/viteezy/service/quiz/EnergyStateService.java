package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.EnergyState;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface EnergyStateService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.ENERGY_STATE, key = "'energyStateId_'+#id")
    Either<Throwable, Optional<EnergyState>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.ENERGY_STATE, key = "'energyState'")
    Either<Throwable, List<EnergyState>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, EnergyState> save(EnergyState energyState);

}