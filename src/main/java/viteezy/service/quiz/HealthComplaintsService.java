package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.HealthComplaints;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface HealthComplaintsService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.HEALTH_COMPLAINTS, key = "'healthComplaintsId_'+#id")
    Either<Throwable, Optional<HealthComplaints>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.HEALTH_COMPLAINTS, key = "'healthComplaints'")
    Either<Throwable, List<HealthComplaints>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, HealthComplaints> save(HealthComplaints healthComplaints);

}