package fr.cnamts.cpam33.ordonnance.domain.ports.out.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.ImportPatientCandidate;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.ExternalPatientId;

public interface FetchPatientGateway {

    ImportPatientCandidate fetchById(ExternalPatientId externalPatientId) throws DomainException;

}
