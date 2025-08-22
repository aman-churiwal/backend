package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.LibidoStressLevel;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface LibidoStressLevelService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.LIBIDO_STRESS_LEVEL, key = "'libidoStressLevelId_'+#id")
    Either<Throwable, Optional<LibidoStressLevel>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.LIBIDO_STRESS_LEVEL, key = "'libidoStressLevel'")
    Either<Throwable, List<LibidoStressLevel>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, LibidoStressLevel> save(LibidoStressLevel libidoStressLevel);

}