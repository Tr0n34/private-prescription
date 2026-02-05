package fr.cnamts.cpam33.ordonnance.domain.ports.out.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;

import java.util.Optional;

public interface PatientRepository extends RepositoryPort<Patient, PatientId> {

    Optional<Patient> findByExternalId(ExternalPatientId externalPatientId);

}
