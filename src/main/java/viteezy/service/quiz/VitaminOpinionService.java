package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.VitaminOpinion;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface VitaminOpinionService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.VITAMIN_OPINION, key = "'vitaminOpinionId_'+#id")
    Either<Throwable, Optional<VitaminOpinion>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.VITAMIN_OPINION, key = "'vitaminOpinion'")
    Either<Throwable, List<VitaminOpinion>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, VitaminOpinion> save(VitaminOpinion vitaminOpinion);

}