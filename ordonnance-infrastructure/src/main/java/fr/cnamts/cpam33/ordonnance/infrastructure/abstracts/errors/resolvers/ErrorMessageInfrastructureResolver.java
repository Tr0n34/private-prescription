package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.resolvers;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorDescriptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.enums.InfraStructureExceptionCode;

import java.util.Map;

public interface ErrorMessageInfrastructureResolver {

    ErrorDescriptor resolve(InfraStructureExceptionCode code);

    ErrorDescriptor resolve(InfraStructureExceptionCode code, Map<String, Object> params);

}
