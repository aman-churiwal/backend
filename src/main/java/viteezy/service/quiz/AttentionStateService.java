package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.AttentionState;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface AttentionStateService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.ATTENTION_STATE, key = "'attentionStateId_'+#id")
    Either<Throwable, Optional<AttentionState>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.ATTENTION_STATE, key = "'attentionState'")
    Either<Throwable, List<AttentionState>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AttentionState> save(AttentionState attentionState);

}