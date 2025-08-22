package ${package}.service.quiz;

import io.vavr.control.Either;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ${package}.domain.quiz.${moduleNamePascalCase};
import ${package}.service.cache.quiz.CacheNamesTemplate;

import java.util.List;
import java.util.Optional;

public interface ${moduleNamePascalCase}Service {

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNamesTemplate.DO_NOT_EVER_USE_THIS_FAKE_CACHE_NAME, key = "'${moduleNameCamelCase}Id_'+#id")
    Either<Throwable, Optional<${moduleNamePascalCase}>> find(Long id);

    @Cacheable(cacheManager = "expiringCacheManager", cacheNames = CacheNamesTemplate.DO_NOT_EVER_USE_THIS_FAKE_CACHE_NAME, key = "'${moduleNameCamelCase}'")
    Either<Throwable, List<${moduleNamePascalCase}>> findAll();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    Either<Throwable, ${moduleNamePascalCase}> save(${moduleNamePascalCase} ${moduleNameCamelCase});

}