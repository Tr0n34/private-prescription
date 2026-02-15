package fr.cnamts.cpam33.ordonnance.domain.ports.out.errors;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.ExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ports.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;

public interface ExceptionRepository extends RepositoryPort<DomainException, ExceptionCode> {

}
