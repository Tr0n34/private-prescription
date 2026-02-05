package fr.cnamts.cpam33.ordonnance.domain.ports.out.errors;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.ExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.ports.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;

public interface ExceptionRepository extends RepositoryPort<DomainException, ExceptionCode> {

}
