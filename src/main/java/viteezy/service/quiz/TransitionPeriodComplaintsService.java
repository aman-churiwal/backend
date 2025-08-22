package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.TransitionPeriodComplaints;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface TransitionPeriodComplaintsService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.TRANSITION_PERIOD_COMPLAINTS, key = "'transitionPeriodComplaintsId_'+#id")
    Either<Throwable, Optional<TransitionPeriodComplaints>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.TRANSITION_PERIOD_COMPLAINTS, key = "'transitionPeriodComplaints'")
    Either<Throwable, List<TransitionPeriodComplaints>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, TransitionPeriodComplaints> save(TransitionPeriodComplaints transitionPeriodComplaints);

}