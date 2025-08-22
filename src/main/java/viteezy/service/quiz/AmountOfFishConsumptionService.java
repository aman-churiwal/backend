package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.AmountOfFishConsumption;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface AmountOfFishConsumptionService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.AMOUNT_OF_FISH_CONSUMPTION, key = "'amountOfFishConsumptionId_'+#id")
    Either<Throwable, Optional<AmountOfFishConsumption>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.AMOUNT_OF_FISH_CONSUMPTION, key = "'amountOfFishConsumption'")
    Either<Throwable, List<AmountOfFishConsumption>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfFishConsumption> save(AmountOfFishConsumption amountOfFishConsumption);

}