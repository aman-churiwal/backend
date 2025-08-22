package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.MenstruationSideIssue;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface MenstruationSideIssueService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.MENSTRUATION_SIDE_ISSUE, key = "'menstruationSideIssueId_'+#id")
    Either<Throwable, Optional<MenstruationSideIssue>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.MENSTRUATION_SIDE_ISSUE, key = "'menstruationSideIssue'")
    Either<Throwable, List<MenstruationSideIssue>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, MenstruationSideIssue> save(MenstruationSideIssue menstruationSideIssue);

}