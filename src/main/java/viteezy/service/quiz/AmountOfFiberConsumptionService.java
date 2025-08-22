package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.AmountOfFiberConsumption;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface AmountOfFiberConsumptionService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.AMOUNT_OF_FIBER_CONSUMPTION, key = "'amountOfFiberConsumptionId_'+#id")
    Either<Throwable, Optional<AmountOfFiberConsumption>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.AMOUNT_OF_FIBER_CONSUMPTION, key = "'amountOfFiberConsumption'")
    Either<Throwable, List<AmountOfFiberConsumption>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfFiberConsumption> save(AmountOfFiberConsumption amountOfFiberConsumption);

}