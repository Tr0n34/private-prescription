package fr.cnamts.cpam33.ordonnance.domain.ports.in.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObjectNotFound;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.Patient;

public interface ProviderPatientPort {

    Patient providePatient(Patient patient) throws DomainObjectNotFound;

}
