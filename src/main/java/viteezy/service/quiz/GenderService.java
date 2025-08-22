package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.Gender;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface GenderService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.GENDER, key = "'genderId_'+#id")
    Either<Throwable, Optional<Gender>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.GENDER, key = "'gender'")
    Either<Throwable, List<Gender>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, Gender> save(Gender gender);

}