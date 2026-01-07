package fr.cnamts.cpam33.ordonnance.domain.ports.out.errors;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.ExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.RepositoryPort;

public interface ExceptionRepository extends RepositoryPort<DomainException, ExceptionCode> {

}
