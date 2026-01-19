package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces;

import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.ActeMetierRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.caches.ActeMetierCache;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.traces.ActeMetierJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class InMemoryActeMetierCache implements ActeMetierCache {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryActeMetierCache.class.getName());

    private final ActeMetierRepository repository;
    private final Map<String, ActeMetierEntity> cache = new ConcurrentHashMap<>();
    private final AtomicBoolean ready = new AtomicBoolean(false);

    public InMemoryActeMetierCache(ActeMetierRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isReady() {
        return ready.get();
    }

    @Override
    public void refresh() {
        Map<String, ActeMetierEntity> datas = new HashMap<>();
        repository.findAll().forEach(e -> {
            logger.trace("{} has been added in cache", e.getCode());
            datas.put(e.getCode(), e);
        });
        cache.clear();
        cache.putAll(datas);
        ready.set(true);
        logger.info("refresh cache. Cache is ready : {}", isReady());
    }

    @Override
    public ActeMetierEntity getRequired(String code) {
        ActeMetierEntity entity = cache.get(code);
        if ( entity == null ) {
            throw new IllegalArgumentException("Acte métier inexistant : " + code);
        }
        return entity;
    }

    @Override
    public Map<String, ActeMetierEntity> snapshot() {
        logger.info("get cache. Cache is ready: {}", isReady());
        return Map.copyOf(cache);
    }

    @Override
    public int size() {
        return cache.size();
    }

}
