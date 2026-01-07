package fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.errors;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.ErrorCatalogEntity;

public record ErrorCatalogJsonDto(
        String code,
        String boundedContext,
        int httpStatus,
        String message,
        String description
) {
    public ErrorCatalogEntity toEntity() {
        return new ErrorCatalogEntity(
                code,
                boundedContext,
                httpStatus,
                message,
                description,
                true
        );
    }
}
