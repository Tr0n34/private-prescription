package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.errors;

import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.enums.CacheExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.caches.ErrorCatalogCache;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.ordonnances.ErrorCatalogJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.ErrorCatalogEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class InMemoryErrorCatalogCache implements ErrorCatalogCache {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryErrorCatalogCache.class.getName());

    private final ErrorCatalogJpaRepository errorCatalogJpaRepository;
    private final Map<String, ErrorCatalogEntity> cache = new ConcurrentHashMap<>();
    private final AtomicBoolean ready = new AtomicBoolean(false);

    public InMemoryErrorCatalogCache(ErrorCatalogJpaRepository errorCatalogJpaRepository) {
        this.errorCatalogJpaRepository = errorCatalogJpaRepository;
    }

    @Override
    public boolean isReady() {
        return ready.get();
    }

    @Override
    public void refresh() {
        Map<String, ErrorCatalogEntity> datas = new HashMap<>();
        errorCatalogJpaRepository.findAll().forEach(error -> {
            logger.trace("{} has been added in cache", error.getCode());
            datas.put(error.getCode(), error);
        });
        cache.clear();
        cache.putAll(datas);
        ready.set(true);
        logger.info("refresh cache error. Cache is ready : {}", isReady());
    }

    @Override
    public Map<String, ErrorCatalogEntity> snapshot() {
        logger.info("get error cache. Cache is ready: {}", isReady());
        return Map.copyOf(cache);
    }

    @Override
    public ErrorCatalogEntity getRequired(String code) {
        ErrorCatalogEntity errorCatalogEntity = cache.get(code);
        if ( errorCatalogEntity == null ) {
            throw new InfrastructureException(CacheExceptionCode.TECH_CACHE_ACTE_METIER_INEXISTANT, Map.of("acteMetier", code));
        }
        logger.trace("cache data found : {}", code);
        return errorCatalogEntity;
    }

    @Override
    public int size() {
        return cache.size();
    }

}
