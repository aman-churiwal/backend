package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.LoseWeightChallenge;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface LoseWeightChallengeService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.LOSE_WEIGHT_CHALLENGE, key = "'loseWeightChallengeId_'+#id")
    Either<Throwable, Optional<LoseWeightChallenge>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.LOSE_WEIGHT_CHALLENGE, key = "'loseWeightChallenge'")
    Either<Throwable, List<LoseWeightChallenge>> findAll();

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.LOSE_WEIGHT_CHALLENGE, key = "'loseWeightChallenge_active'")
    Either<Throwable, List<LoseWeightChallenge>> findActive();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, LoseWeightChallenge> save(LoseWeightChallenge loseWeightChallenge);

}