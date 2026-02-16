package fr.cnamts.cpam33.ordonnance.infrastructure.kernel.errors;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.ExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.ErrorDescriptor;

import java.util.Map;

public interface ErrorMessageDomainResolver {

    ErrorDescriptor resolve(ExceptionCode code, Map<String, Object> params);

}

