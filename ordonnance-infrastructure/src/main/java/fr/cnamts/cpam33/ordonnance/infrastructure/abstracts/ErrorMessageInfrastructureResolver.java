package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts;

import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.ErrorDescriptor;

public interface ErrorMessageInfrastructureResolver {

    ErrorDescriptor resolve(InfraStructureExceptionCode code);

    ErrorDescriptor resolve(InfraStructureExceptionCode code, String[] placeHolders);

}
