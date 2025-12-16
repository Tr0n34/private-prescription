package fr.cnamts.cpam33.ordonnance.domain.ports.externals;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.IdentitePatient;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.PatientId;

import java.util.List;

public interface PatientInfoPort {

    IdentitePatient fetchPatient(PatientId patientId);

    List<IdentitePatient> fetchPatients(List<PatientId> patientIds);

}
