package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.errors;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.ordonnances.ErrorCatalogJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.ErrorCatalogEntity;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ErrorCatalogPersistanceAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ErrorCatalogPersistanceAdapter.class);

    private final ErrorCatalogJpaRepository errorCatalogJpaRepository;

    public ErrorCatalogPersistanceAdapter(ErrorCatalogJpaRepository errorCatalogJpaRepository) {
        this.errorCatalogJpaRepository = errorCatalogJpaRepository;
    }

    public List<ErrorCatalogEntity> findAll() {
        return errorCatalogJpaRepository.findAll();
    }

    public void saveOrUpdate(ErrorCatalogEntity incoming) {
        errorCatalogJpaRepository.findById(incoming.getCode())
                .ifPresentOrElse(
                        existing -> update(existing, incoming),
                        () -> errorCatalogJpaRepository.save(incoming)
                );
    }

    private void update(ErrorCatalogEntity existing, ErrorCatalogEntity incoming) {
        existing.deactivate();
        ErrorCatalogEntity updated = new ErrorCatalogEntity(
                incoming.getCode(),
                incoming.getBoundedContext(),
                incoming.getHttpStatus(),
                incoming.getMessage(),
                incoming.getDescription(),
                true
        );
        logger.debug("Mise à jour du catalogue : {}", updated.toString());
        errorCatalogJpaRepository.save(updated);
    }

}