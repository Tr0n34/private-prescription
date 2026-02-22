package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.actuators.metrics.ErrorCatalogMetrics;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.events.ActeMetierLoadedEvent;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.events.ErrorLoadedEvent;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.caches.ErrorCatalogCache;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.errors.ErrorCatalogPersistanceAdapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.ErrorCatalogEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class ErrorCatalogLoader {

    private static final Logger logger = LoggerFactory.getLogger(ErrorCatalogLoader.class);

    @Value("${ordonnance.loaders.errors.file:classpath:errors.json}")
    private String errorFile;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ErrorCatalogPersistanceAdapter errorCatalogPersistanceAdapter;
    private final ObjectMapper mapper;
    private final ResourceLoader resourceLoader;
    private final ErrorCatalogMetrics errorCatalogMetrics;
    private final ErrorCatalogCache errorCatalogCache;


    public ErrorCatalogLoader(ApplicationEventPublisher applicationEventPublisher,
                              ErrorCatalogPersistanceAdapter errorCatalogPersistanceAdapter,
                              ObjectMapper mapper,
                              ResourceLoader resourceLoader,
                              ErrorCatalogMetrics errorCatalogMetrics,
                              ErrorCatalogCache errorCatalogCache) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.errorCatalogPersistanceAdapter = errorCatalogPersistanceAdapter;
        this.mapper = mapper;
        this.resourceLoader = resourceLoader;
        this.errorCatalogMetrics = errorCatalogMetrics;
        this.errorCatalogCache = errorCatalogCache;
    }

    public synchronized void reload() {
        errorCatalogMetrics.recordReload( () -> {
            try {
                Resource resource = resourceLoader.getResource(errorFile);
                try ( InputStream is = resource.getInputStream() ) {
                    List<ErrorCatalogEntity> errors = mapper.readValue(is, new TypeReference<>() {});
                    for ( ErrorCatalogEntity error : errors ) {
                        errorCatalogPersistanceAdapter.saveOrUpdate(error);
                    }
                }
                logger.info("Error catalog reloaded from {}", errorFile);
                errorCatalogCache.refresh();
                applicationEventPublisher.publishEvent(new ErrorLoadedEvent(
                        UUID.randomUUID(),
                        Instant.now()
                ));
            } catch (Exception e) {
                logger.error("Failed to reload error catalog", e);
            }
        });
    }

}
