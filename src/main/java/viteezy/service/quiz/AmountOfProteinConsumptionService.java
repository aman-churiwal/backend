package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.AmountOfProteinConsumption;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface AmountOfProteinConsumptionService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.AMOUNT_OF_PROTEIN_CONSUMPTION, key = "'amountOfProteinConsumptionId_'+#id")
    Either<Throwable, Optional<AmountOfProteinConsumption>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.AMOUNT_OF_PROTEIN_CONSUMPTION, key = "'amountOfProteinConsumption'")
    Either<Throwable, List<AmountOfProteinConsumption>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AmountOfProteinConsumption> save(AmountOfProteinConsumption amountOfProteinConsumption);

}