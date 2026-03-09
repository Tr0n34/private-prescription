package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.resolvers;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.ExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorDescriptor;

import java.util.Map;

public interface ErrorMessageDomainResolver {

    ErrorDescriptor resolve(ExceptionCode code, Map<String, Object> params);

}

