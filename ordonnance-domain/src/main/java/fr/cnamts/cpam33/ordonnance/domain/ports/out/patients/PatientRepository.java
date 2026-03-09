package fr.cnamts.cpam33.ordonnance.domain.ports.out.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.ports.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.PatientId;

import java.util.Optional;

public interface PatientRepository extends RepositoryPort<Patient, PatientId> {

    Optional<Patient> findByExternalId(ExternalPatientId externalPatientId);

    Optional<Patient> findByExternalIdOrThrow(ExternalPatientId externalPatientId);

}
