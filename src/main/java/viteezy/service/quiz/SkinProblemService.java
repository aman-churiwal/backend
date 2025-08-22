package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.SkinProblem;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface SkinProblemService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.SKIN_PROBLEM, key = "'skinProblemId_'+#id")
    Either<Throwable, Optional<SkinProblem>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.SKIN_PROBLEM, key = "'skinProblem'")
    Either<Throwable, List<SkinProblem>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, SkinProblem> save(SkinProblem skinProblem);

}