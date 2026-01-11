package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.ExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.ErrorDescriptor;

public interface ErrorMessageDomainResolver {

    ErrorDescriptor resolve(ExceptionCode code);

}

