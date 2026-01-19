package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.errors;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.ExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorMessageDomainResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorMessageInfrastructureResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfraStructureExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.ErrorDescriptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.ErrorCatalogEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.ordonnances.ErrorCatalogJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DatabaseErrorMessageResolver implements ErrorMessageDomainResolver, ErrorMessageInfrastructureResolver, Adapter {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseErrorMessageResolver.class);

    public static final String UNRESOLVED_ERROR_MESSAGE = "Error code not found : %s";
    public static final int NO_PLACEHOLDERS = 0;
    public static final String[] UNDEFINED_PLACEHOLDERS = null;

    private final ErrorCatalogJpaRepository errorCatalogJpaRepository;

    public DatabaseErrorMessageResolver(ErrorCatalogJpaRepository errorCatalogJpaRepository) {
        this.errorCatalogJpaRepository = errorCatalogJpaRepository;
    }

    @Override
    public ErrorDescriptor resolve(ExceptionCode exceptionCode) {
        return resolveByCode(exceptionCode.toString(), UNDEFINED_PLACEHOLDERS);
    }

    @Override
    public ErrorDescriptor resolve(InfraStructureExceptionCode code) {
        return resolveByCode(code.toString(), UNDEFINED_PLACEHOLDERS);
    }

    @Override
    public ErrorDescriptor resolve(InfraStructureExceptionCode code, String[] placeHolders) {
        return resolveByCode(code.toString(), placeHolders);
    }

    public ErrorDescriptor resolveByCode(String exceptionCode, String[] placeHolders) {
        ErrorCatalogEntity entity = errorCatalogJpaRepository.findByCodeAndActiveTrue(exceptionCode).orElseThrow(
                () -> new IllegalStateException(String.format(UNRESOLVED_ERROR_MESSAGE, exceptionCode))
        );
        String message = ( placeHolders == null || placeHolders.length == NO_PLACEHOLDERS)
                ? entity.getMessage()
                : String.format(entity.getMessage(), (Object[]) placeHolders);
        logger.debug("{} : {}", exceptionCode, message);
        return ErrorDescriptor.of(
                entity.getCode(),
                message,
                entity.getHttpStatus(),
                LocalDateTime.now(),
                entity.getBoundedContext());
    }

}
