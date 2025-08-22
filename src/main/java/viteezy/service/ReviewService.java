package viteezy.service;

import io.vavr.control.Try;
import org.springframework.cache.annotation.Cacheable;
import viteezy.domain.Review;
import viteezy.service.cache.CacheNames;

public interface ReviewService {

    @Cacheable(cacheManager = "dayLivedCacheManager", cacheNames = CacheNames.REVIEW, key = "'review_'+#source")
    Try<Review> summary(String source);
}
