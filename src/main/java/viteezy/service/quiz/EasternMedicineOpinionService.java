package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.EasternMedicineOpinion;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface EasternMedicineOpinionService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.EASTERN_MEDICINE_OPINION, key = "'easternMedicineOpinionId_'+#id")
    Either<Throwable, Optional<EasternMedicineOpinion>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.EASTERN_MEDICINE_OPINION, key = "'easternMedicineOpinion'")
    Either<Throwable, List<EasternMedicineOpinion>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, EasternMedicineOpinion> save(EasternMedicineOpinion easternMedicineOpinion);

}