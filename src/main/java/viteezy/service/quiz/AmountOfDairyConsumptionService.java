package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.AmountOfDairyConsumption;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface AmountOfDairyConsumptionService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.AMOUNT_OF_DAIRY_CONSUMPTION, key = "'amountOfDairyConsumptionId_'+#id")
    Either<Throwable, Optional<AmountOfDairyConsumption>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.AMOUNT_OF_DAIRY_CONSUMPTION, key = "'amountOfDairyConsumption'")
    Either<Throwable, List<AmountOfDairyConsumption>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfDairyConsumption> save(AmountOfDairyConsumption amountOfDairyConsumption);

}