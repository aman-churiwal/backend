package viteezy.service;

import io.vavr.control.Try;
import org.springframework.cache.annotation.Cacheable;
import viteezy.controller.dto.BundleGetResponse;
import viteezy.service.cache.CacheNames;

import java.util.List;

public interface BundleService {

    @Cacheable(cacheManager = "hourLivedCacheManager", cacheNames = CacheNames.BUNDLES)
    Try<List<BundleGetResponse>> findAllBundles();
}
