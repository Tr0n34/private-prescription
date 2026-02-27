package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.caches;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.ActeMetierRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.caches.ActeMetierCache;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.enums.CacheExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class InMemoryActeMetierCache implements ActeMetierCache {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryActeMetierCache.class.getName());

    private final ActeMetierRepository acteMetierRepository;
    private final Map<String, ActeMetier> cache = new ConcurrentHashMap<>();
    private final AtomicBoolean ready = new AtomicBoolean(false);

    public InMemoryActeMetierCache(ActeMetierRepository acteMetierRepository) {
        this.acteMetierRepository = acteMetierRepository;
    }

    @Override
    public boolean isReady() {
        return ready.get();
    }

    @Override
    public void refresh() {
        Map<String, ActeMetier> datas = new HashMap<>();
        acteMetierRepository.findAll().forEach(acteMetier -> {
            logger.trace("{} has been added in cache", acteMetier.acteMetierId().code());
            datas.put(acteMetier.acteMetierId().code(), acteMetier);
        });
        cache.clear();
        cache.putAll(datas);
        ready.set(true);
        logger.info("refresh cache. Cache is ready : {}", isReady());
    }

    @Override
    public ActeMetier getRequired(String code) {
        ActeMetier acteMetier = cache.get(code);
        if ( acteMetier == null ) {
            throw new InfrastructureException(CacheExceptionCode.TECH_CACHE_ACTE_METIER_INEXISTANT, Map.of("acteMetier", code));
        }
        return acteMetier;
    }

    @Override
    public Map<String, ActeMetier> snapshot() {
        logger.info("get cache. Cache is ready: {}", isReady());
        return Map.copyOf(cache);
    }

    @Override
    public int size() {
        return cache.size();
    }

}
