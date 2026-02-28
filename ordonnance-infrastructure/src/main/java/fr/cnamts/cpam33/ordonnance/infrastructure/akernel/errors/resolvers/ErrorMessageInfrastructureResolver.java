package fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.resolvers;

import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.ErrorDescriptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.enums.InfraStructureExceptionCode;

import java.util.Map;

public interface ErrorMessageInfrastructureResolver {

    ErrorDescriptor resolve(InfraStructureExceptionCode code);

    ErrorDescriptor resolve(InfraStructureExceptionCode code, Map<String, Object> params);

}
