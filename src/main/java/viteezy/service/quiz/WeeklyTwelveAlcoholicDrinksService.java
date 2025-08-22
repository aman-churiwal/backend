package viteezy.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import viteezy.domain.quiz.WeeklyTwelveAlcoholicDrinks;
import viteezy.service.cache.CacheNames;

import java.util.List;
import java.util.Optional;

public interface WeeklyTwelveAlcoholicDrinksService {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.WEEKLY_TWELVE_ALCOHOLIC_DRINKS, key = "'weeklyTwelveAlcoholicDrinksId_'+#id")
    Either<Throwable, Optional<WeeklyTwelveAlcoholicDrinks>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNames.WEEKLY_TWELVE_ALCOHOLIC_DRINKS, key = "'weeklyTwelveAlcoholicDrinks'")
    Either<Throwable, List<WeeklyTwelveAlcoholicDrinks>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, WeeklyTwelveAlcoholicDrinks> save(WeeklyTwelveAlcoholicDrinks weeklyTwelveAlcoholicDrinks);

}