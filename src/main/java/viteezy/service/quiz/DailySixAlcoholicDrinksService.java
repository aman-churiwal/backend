package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.DailySixAlcoholicDrinks;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface DailySixAlcoholicDrinksService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.DAILY_SIX_ALCOHOLIC_DRINKS, key = "'dailySixAlcoholicDrinksId_'+#id")
    Either<Throwable, Optional<DailySixAlcoholicDrinks>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.DAILY_SIX_ALCOHOLIC_DRINKS, key = "'dailySixAlcoholicDrinks'")
    Either<Throwable, List<DailySixAlcoholicDrinks>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, DailySixAlcoholicDrinks> save(DailySixAlcoholicDrinks dailySixAlcoholicDrinks);

}