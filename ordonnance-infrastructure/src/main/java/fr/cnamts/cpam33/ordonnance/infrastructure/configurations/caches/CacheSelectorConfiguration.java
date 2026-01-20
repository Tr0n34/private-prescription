package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.caches;

import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.ActeMetierRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.InMemoryActeMetierCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheSelectorConfiguration {

    @Bean
    @ConditionalOnProperty(name = "ordonnance.cache.type", havingValue = "ignite")
    InMemoryActeMetierCache acteMetierCacheIgnite(ActeMetierRepository acteMetierRepository) {
        return null;
    }

    @Bean
    @ConditionalOnProperty(name = "ordonnance.cache.type", havingValue = "inmemory")
    InMemoryActeMetierCache acteMetierCacheInMemory(ActeMetierRepository acteMetierRepository) {
        return new InMemoryActeMetierCache(acteMetierRepository);
    }

    @Bean
    @ConditionalOnMissingBean(name = "ordonnance.cache.type")
    InMemoryActeMetierCache defaultActeMetierCache(ActeMetierRepository acteMetierRepository) {
        return new InMemoryActeMetierCache(acteMetierRepository);
    }



}
