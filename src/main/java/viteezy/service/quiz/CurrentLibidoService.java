package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.CurrentLibido;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface CurrentLibidoService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.CURRENT_LIBIDO, key = "'currentLibidoId_'+#id")
    Either<Throwable, Optional<CurrentLibido>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.CURRENT_LIBIDO, key = "'currentLibido'")
    Either<Throwable, List<CurrentLibido>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, CurrentLibido> save(CurrentLibido currentLibido);

}