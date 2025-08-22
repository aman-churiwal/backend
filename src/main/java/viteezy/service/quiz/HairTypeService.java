package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.HairType;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface HairTypeService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.HAIR_TYPE, key = "'hairTypeId_'+#id")
    Either<Throwable, Optional<HairType>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.HAIR_TYPE, key = "'hairType'")
    Either<Throwable, List<HairType>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, HairType> save(HairType hairType);

}