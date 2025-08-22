package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.SkinType;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface SkinTypeService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.SKIN_TYPE, key = "'skinTypeId_'+#id")
    Either<Throwable, Optional<SkinType>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.SKIN_TYPE, key = "'skinType'")
    Either<Throwable, List<SkinType>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SkinType> save(SkinType skinType);

}