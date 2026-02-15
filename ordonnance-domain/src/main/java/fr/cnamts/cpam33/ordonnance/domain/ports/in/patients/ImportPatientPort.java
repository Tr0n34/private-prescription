package fr.cnamts.cpam33.ordonnance.domain.ports.in.patients;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.ImportPatientCandidate;

public interface ImportPatientPort {

    Patient importerPatient(ExternalPatientId externalPatientId);

}
