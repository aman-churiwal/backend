package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.TypeOfTraining;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface TypeOfTrainingService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.TYPE_OF_TRAINING, key = "'typeOfTrainingId_'+#id")
    Either<Throwable, Optional<TypeOfTraining>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.TYPE_OF_TRAINING, key = "'typeOfTraining'")
    Either<Throwable, List<TypeOfTraining>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, TypeOfTraining> save(TypeOfTraining typeOfTraining);

}