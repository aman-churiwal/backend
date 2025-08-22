package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.NewProductAvailable;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface NewProductAvailableService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.NEW_PRODUCT_AVAILABLE, key = "'newProductAvailableId_'+#id")
    Either<Throwable, Optional<NewProductAvailable>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.NEW_PRODUCT_AVAILABLE, key = "'newProductAvailable'")
    Either<Throwable, List<NewProductAvailable>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, NewProductAvailable> save(NewProductAvailable newProductAvailable);

}