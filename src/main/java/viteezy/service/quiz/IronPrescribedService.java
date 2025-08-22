package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.IronPrescribed;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface IronPrescribedService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.IRON_PRESCRIBED, key = "'ironPrescribedId_'+#id")
    Either<Throwable, Optional<IronPrescribed>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.IRON_PRESCRIBED, key = "'ironPrescribed'")
    Either<Throwable, List<IronPrescribed>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, IronPrescribed> save(IronPrescribed ironPrescribed);

}