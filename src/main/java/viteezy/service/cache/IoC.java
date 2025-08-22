package viteezy.service.cache;

import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import viteezy.configuration.ViteezyConfiguration;
import viteezy.configuration.cache.CacheConfiguration;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Configuration("cacheConfiguration")
@EnableCaching
public class IoC {

    private final ViteezyConfiguration configuration;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public IoC(ViteezyConfiguration configuration) {
        this.configuration = configuration;
    }

    @Bean("expiringCacheManager")
    @Primary
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(getCacheNames()) {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(name, getLongLivedCacheStore(), false);
            }
        };
    }

    @Bean("dayLivedCacheManager")
    public CacheManager dayLivedCacheManager() {
        return new ConcurrentMapCacheManager(getCacheNames()) {
            @NonNull
            @Override
            protected Cache createConcurrentMapCache(@NonNull String name) {
                return new ConcurrentMapCache(name, getDayLivedCacheStore(), false);
            }
        };
    }

    @Bean("hourLivedCacheManager")
    public CacheManager hourLivedCacheManager() {
        return new ConcurrentMapCacheManager(getCacheNames()) {
            @NonNull
            @Override
            protected Cache createConcurrentMapCache(@NonNull String name) {
                return new ConcurrentMapCache(name, getHourLivedCacheStore(), false);
            }
        };
    }

    private String[] getCacheNames() {
        return new String[]{
                CacheNames.ACNE_PLACE,
                CacheNames.AMOUNT_OF_DAIRY_CONSUMPTION,
                CacheNames.AMOUNT_OF_FIBER_CONSUMPTION,
                CacheNames.AMOUNT_OF_FISH_CONSUMPTION,
                CacheNames.AMOUNT_OF_FRUIT_CONSUMPTION,
                CacheNames.AMOUNT_OF_MEAT_CONSUMPTION,
                CacheNames.AMOUNT_OF_PROTEIN_CONSUMPTION,
                CacheNames.AMOUNT_OF_VEGETABLE_CONSUMPTION,
                CacheNames.ALLERGY,
                CacheNames.ALLERGY_TYPE,
                CacheNames.ATTENTION_FOCUS,
                CacheNames.ATTENTION_STATE,
                CacheNames.AVERAGE_SLEEPING_HOURS,
                CacheNames.BINGE_EATING,
                CacheNames.BINGE_EATING_REASON,
                CacheNames.BIRTH_HEALTH,
                CacheNames.BLEND_INGREDIENT_REASON,
                CacheNames.BODY_TYPE_CURRENT,
                CacheNames.BODY_TYPE_TARGET,
                CacheNames.BUNDLES,
                CacheNames.AMOUNT_OF_TRAINING,
                CacheNames.CHILDREN_WISH,
                CacheNames.CONTENT,
                CacheNames.CURRENT_LIBIDO,
                CacheNames.DAILY_FOUR_COFFEE,
                CacheNames.DAILY_SIX_ALCOHOLIC_DRINKS,
                CacheNames.DIET_INTOLERANCE,
                CacheNames.DIET_TYPE,
                CacheNames.DIGESTION_AMOUNT,
                CacheNames.DIGESTION_OCCURRENCE,
                CacheNames.DRY_SKIN,
                CacheNames.EASTERN_MEDICINE_OPINION,
                CacheNames.ENERGY_LEVEL,
                CacheNames.ENERGY_STATE,
                CacheNames.FACEBOOK_ACCESS_TOKEN,
                CacheNames.FIND_US,
                CacheNames.FISH_CONSUMPTION,
                CacheNames.FRUIT_CONSUMPTION,
                CacheNames.GENDER,
                CacheNames.HAIR_TYPE,
                CacheNames.HEALTH_COMPLAINTS,
                CacheNames.HEALTHY_LIFESTYLE,
                CacheNames.HELP_GOAL,
                CacheNames.HOURS_OF_SLEEP,
                CacheNames.INGREDIENTS,
                CacheNames.INGREDIENT_PRICES,
                CacheNames.IRON_PRESCRIBED,
                CacheNames.LACK_OF_CONCENTRATION,
                CacheNames.LIBIDO_STRESS_LEVEL,
                CacheNames.LOSE_WEIGHT_CHALLENGE,
                CacheNames.MENSTRUATION_INTERVAL,
                CacheNames.MENSTRUATION_MOOD,
                CacheNames.MENSTRUATION_SIDE_ISSUE,
                CacheNames.MENTAL_FITNESS,
                CacheNames.MEDICATION,
                CacheNames.MINUTES_OF_SUN,
                CacheNames.NAIL_IMPROVEMENT,
                CacheNames.NEW_PRODUCT_AVAILABLE,
                CacheNames.NUTRITION_CONSUMPTION,
                CacheNames.OFTEN_HAVING_FLUE,
                CacheNames.PAYMENT_METHOD,
                CacheNames.PREGNANCY,
                CacheNames.PREGNANCY_STATE,
                CacheNames.PRESENT_AT_CROWDED_PLACES,
                CacheNames.PRIMARY_GOAL,
                CacheNames.REVIEW,
                CacheNames.SKIN_PROBLEM,
                CacheNames.SKIN_TYPE,
                CacheNames.SLEEP_HOURS_LESS_THAN_SEVEN,
                CacheNames.SLEEP_QUALITY,
                CacheNames.SMOKE,
                CacheNames.SPORT_AMOUNT,
                CacheNames.SPORT_TYPE,
                CacheNames.SPORT_REASON,
                CacheNames.STRESS_LEVEL,
                CacheNames.STRESS_LEVEL_CONDITION,
                CacheNames.STRESS_LEVEL_AT_END_OF_DAY,
                CacheNames.THIRTY_MINUTES_OF_SUN,
                CacheNames.TIRED_WHEN_WAKE_UP,
                CacheNames.TRAINING_INTENSIVELY,
                CacheNames.TRANSITION_PERIOD_COMPLAINTS,
                CacheNames.TROUBLE_FALLING_ASLEEP,
                CacheNames.TYPE_OF_TRAINING,
                CacheNames.URINARY_INFECTION,
                CacheNames.USAGE_GOAL,
                CacheNames.USAGE_REASON,
                CacheNames.USED_SUPPLEMENTS,
                CacheNames.VEGETABLE_CONSUMPTION,
                CacheNames.VITAMIN_INTAKE,
                CacheNames.VITAMIN_OPINION,
                CacheNames.WEEKLY_TWELVE_ALCOHOLIC_DRINKS
        };
    }

    private ConcurrentMap<Object, Object> getLongLivedCacheStore() {
        CacheConfiguration cacheConfiguration = configuration.getCacheConfiguration();
        return CacheBuilder.newBuilder()
                .expireAfterWrite(cacheConfiguration.getTtlSeconds(), TimeUnit.SECONDS)
                .maximumSize(cacheConfiguration.getMaxSize())
                .build()
                .asMap();
    }

    private ConcurrentMap<Object, Object> getDayLivedCacheStore() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS)
                .build()
                .asMap();
    }

    private ConcurrentMap<Object, Object> getHourLivedCacheStore() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build()
                .asMap();
    }
}
