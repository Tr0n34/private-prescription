package fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors;

import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.ErrorDescriptor;

import java.util.Map;

public interface ErrorMessageInfrastructureResolver {

    ErrorDescriptor resolve(InfraStructureExceptionCode code);

    ErrorDescriptor resolve(InfraStructureExceptionCode code, Map<String, Object> params);

}
