package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.VitaminIntake;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface VitaminIntakeService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.VITAMIN_INTAKE, key = "'vitaminIntakeId_'+#id")
    Either<Throwable, Optional<VitaminIntake>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.VITAMIN_INTAKE, key = "'vitaminIntake'")
    Either<Throwable, List<VitaminIntake>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, VitaminIntake> save(VitaminIntake vitaminIntake);

}