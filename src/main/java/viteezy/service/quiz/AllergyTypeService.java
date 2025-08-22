package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.AllergyType;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface AllergyTypeService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.ALLERGY_TYPE, key = "'allergyTypeId_'+#id")
    Either<Throwable, Optional<AllergyType>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.ALLERGY_TYPE, key = "'allergyType'")
    Either<Throwable, List<AllergyType>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AllergyType> save(AllergyType allergyType);

}