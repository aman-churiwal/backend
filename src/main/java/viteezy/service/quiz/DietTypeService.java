package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.DietType;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface DietTypeService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.DIET_TYPE, key = "'dietTypeId_'+#id")
    Either<Throwable, Optional<DietType>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.DIET_TYPE, key = "'dietType'")
    Either<Throwable, List<DietType>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DietType> save(DietType dietType);

}