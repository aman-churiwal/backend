package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.MentalFitness;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface MentalFitnessService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.MENTAL_FITNESS, key = "'mentalFitnessId_'+#id")
    Either<Throwable, Optional<MentalFitness>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.MENTAL_FITNESS, key = "'mentalFitness'")
    Either<Throwable, List<MentalFitness>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, MentalFitness> save(MentalFitness mentalFitness);

}