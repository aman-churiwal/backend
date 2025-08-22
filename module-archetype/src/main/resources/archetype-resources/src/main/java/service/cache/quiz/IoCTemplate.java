package ${package}.service.cache.quiz;

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

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Configuration("cacheConfiguration")
@EnableCaching
public class IoCTemplate {

    // TODO. Update the real IoC with the new cache value.
    private String[] getCacheNames() {
        return new String[]{
                CacheNamesTemplate.DO_NOT_EVER_USE_THIS_FAKE_CACHE_NAME
        };
    }

}
