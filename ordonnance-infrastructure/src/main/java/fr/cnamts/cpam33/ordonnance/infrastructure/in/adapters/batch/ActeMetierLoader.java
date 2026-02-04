package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.actuators.metrics.ActeMetierMetrics;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.events.ActeMetierLoadedEvent;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.ActeMetierPersistanceAdapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.InMemoryActeMetierCache;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.ActeMetierEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class ActeMetierLoader {

    private static final Logger logger = LoggerFactory.getLogger(ActeMetierLoader.class);

    @Value("${ordonnance.loaders.actesMetiers.file:classpath:actes_metiers.json}")
    private String actesMetiersFile;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ActeMetierPersistanceAdapter acteMetierPersistanceAdapter;
    private final ObjectMapper mapper;
    private final ResourceLoader resourceLoader;
    private final ActeMetierMetrics acteMetierMetrics;
    private final ActeMetierEntityMapper acteMetierEntityMapper;
    private final InMemoryActeMetierCache acteMetierCache;

    public ActeMetierLoader(ApplicationEventPublisher applicationEventPublisher,
                            ActeMetierPersistanceAdapter acteMetierPersistanceAdapter,
                            ObjectMapper mapper, ResourceLoader resourceLoader,
                            ActeMetierMetrics acteMetierMetrics,
                            ActeMetierEntityMapper acteMetierEntityMapper,
                            InMemoryActeMetierCache acteMetierCache) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.acteMetierPersistanceAdapter = acteMetierPersistanceAdapter;
        this.mapper = mapper;
        this.resourceLoader = resourceLoader;
        this.acteMetierMetrics = acteMetierMetrics;
        this.acteMetierEntityMapper = acteMetierEntityMapper;
        this.acteMetierCache = acteMetierCache;
    }

    public synchronized void reload() {
        acteMetierMetrics.recordReload( () -> {
            try {
                Resource resource = resourceLoader.getResource(actesMetiersFile);
                try ( InputStream is = resource.getInputStream() ) {
                    List<ActeMetierEntity> acteMetiers = mapper.readValue(is, new TypeReference<>() {});
                    for ( ActeMetierEntity acteMetier : acteMetiers ) {
                        logger.info("load : {}", acteMetier.toString());
                        acteMetierPersistanceAdapter.save(acteMetierEntityMapper.toDomain(acteMetier));
                    }
                }
                logger.info("Acte Metier reloaded from {}", actesMetiersFile);
                acteMetierCache.refresh();
                applicationEventPublisher.publishEvent(new ActeMetierLoadedEvent());

            } catch (Exception e) {
                logger.error("Failed to reload Acte Metier", e);
            }
        });
    }

}
