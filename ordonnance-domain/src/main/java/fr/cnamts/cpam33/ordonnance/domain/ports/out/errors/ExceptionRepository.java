package fr.cnamts.cpam33.ordonnance.domain.ports.out.errors;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.ExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ports.RepositoryPort;

public interface ExceptionRepository extends RepositoryPort<DomainException, ExceptionCode> {

}
