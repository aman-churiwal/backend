package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.AmountOfVegetableConsumption;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface AmountOfVegetableConsumptionService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.AMOUNT_OF_VEGETABLE_CONSUMPTION, key = "'amountOfVegetableConsumptionId_'+#id")
    Either<Throwable, Optional<AmountOfVegetableConsumption>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.AMOUNT_OF_VEGETABLE_CONSUMPTION, key = "'amountOfVegetableConsumption'")
    Either<Throwable, List<AmountOfVegetableConsumption>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfVegetableConsumption> save(AmountOfVegetableConsumption amountOfVegetableConsumption);

}