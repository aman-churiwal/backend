package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.ChildrenWish;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface ChildrenWishService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.CHILDREN_WISH, key = "'childrenWishId_'+#id")
    Either<Throwable, Optional<ChildrenWish>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.CHILDREN_WISH, key = "'childrenWish'")
    Either<Throwable, List<ChildrenWish>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, ChildrenWish> save(ChildrenWish childrenWish);

}