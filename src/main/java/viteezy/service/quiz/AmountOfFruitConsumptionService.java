package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.AmountOfFruitConsumption;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface AmountOfFruitConsumptionService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.AMOUNT_OF_FRUIT_CONSUMPTION, key = "'amountOfFruitConsumptionId_'+#id")
    Either<Throwable, Optional<AmountOfFruitConsumption>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.AMOUNT_OF_FRUIT_CONSUMPTION, key = "'amountOfFruitConsumption'")
    Either<Throwable, List<AmountOfFruitConsumption>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfFruitConsumption> save(AmountOfFruitConsumption amountOfFruitConsumption);

}