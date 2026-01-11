package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.traces.ActeMetierJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ActeMetierCache {

    private final ActeMetierJpaRepository repository;
    private final Map<String, ActeMetierEntity> cache = new ConcurrentHashMap<>();
    private final AtomicBoolean ready = new AtomicBoolean(false);

    public ActeMetierCache(ActeMetierJpaRepository repository) {
        this.repository = repository;
    }

    public boolean isReady() {
        return ready.get();
    }

    public void refresh() {
        Map<String, ActeMetierEntity> tmp = new HashMap<>();
        repository.findAll().forEach(e -> tmp.put(e.getCode(), e));
        cache.clear();
        cache.putAll(tmp);
        ready.set(true);
    }

    public ActeMetierEntity getRequired(String code) {
        ActeMetierEntity entity = cache.get(code);
        if ( entity == null ) {
            throw new IllegalArgumentException("Acte métier inexistant : " + code);
        }
        return entity;
    }

}
