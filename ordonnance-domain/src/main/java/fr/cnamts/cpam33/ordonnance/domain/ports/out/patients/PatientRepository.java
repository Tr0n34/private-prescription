package fr.cnamts.cpam33.ordonnance.domain.ports.out.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.PatientId;

public interface PatientRepository extends RepositoryPort<Patient, PatientId> {

}
