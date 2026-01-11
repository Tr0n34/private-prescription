package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.errors;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.ExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorMessageDomainResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorMessageInfrastructureResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfraStructureExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.ErrorDescriptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.ErrorCatalogEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.ordonnances.ErrorCatalogJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DatabaseErrorMessageResolver implements ErrorMessageDomainResolver, ErrorMessageInfrastructureResolver, Adapter {

    public static final String UNRESOLVED_ERROR_MESSAGE = "Error code not found : %s";

    private final ErrorCatalogJpaRepository errorCatalogJpaRepository;

    public DatabaseErrorMessageResolver(ErrorCatalogJpaRepository errorCatalogJpaRepository) {
        this.errorCatalogJpaRepository = errorCatalogJpaRepository;
    }

    @Override
    public ErrorDescriptor resolve(ExceptionCode exceptionCode) {
        return resolveByCode(exceptionCode.toString(), null);
    }

    @Override
    public ErrorDescriptor resolve(InfraStructureExceptionCode code) {
        return resolveByCode(code.toString(), null);
    }

    @Override
    public ErrorDescriptor resolve(InfraStructureExceptionCode code, String[] placeHolders) {
        return resolveByCode(code.toString(), placeHolders);
    }

    public ErrorDescriptor resolveByCode(String exceptionCode, String[] placeHolders) {
        ErrorCatalogEntity entity = errorCatalogJpaRepository.findByCodeAndActiveTrue(exceptionCode).orElseThrow(
                () -> new IllegalStateException(String.format(UNRESOLVED_ERROR_MESSAGE, exceptionCode))
        );
        String message = ( placeHolders == null || placeHolders.length == 0 )
                ? entity.getMessage()
                : String.format(entity.getMessage(), (Object[]) placeHolders);
        return ErrorDescriptor.of(
                entity.getCode(),
                message,
                entity.getHttpStatus(),
                LocalDateTime.now(),
                entity.getBoundedContext());
    }

}
