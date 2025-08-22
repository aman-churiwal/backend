package viteezy.service;

import io.vavr.control.Try;
import org.springframework.cache.annotation.Cacheable;
import viteezy.domain.WebsiteContent;
import viteezy.service.cache.CacheNames;

public interface ContentService {

    @Cacheable(cacheManager = "hourLivedCacheManager", cacheNames = CacheNames.CONTENT, key = "'content_'+#code")
    Try<WebsiteContent> findByCode(String code);
}
