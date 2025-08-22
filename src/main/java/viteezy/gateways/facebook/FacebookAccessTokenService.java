package viteezy.gateways.facebook;

import io.vavr.control.Try;
import org.springframework.cache.annotation.Cacheable;
import viteezy.domain.ConfigurationDatabaseObject;
import viteezy.service.cache.CacheNames;

public interface FacebookAccessTokenService {

    @Cacheable(cacheManager = "hourLivedCacheManager", cacheNames = CacheNames.FACEBOOK_ACCESS_TOKEN)
    Try<ConfigurationDatabaseObject> find();
}
