package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.AttentionFocus;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface AttentionFocusService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.ATTENTION_FOCUS, key = "'attentionFocusId_'+#id")
    Either<Throwable, Optional<AttentionFocus>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.ATTENTION_FOCUS, key = "'attentionFocus'")
    Either<Throwable, List<AttentionFocus>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, AttentionFocus> save(AttentionFocus attentionFocus);

}