package fr.cnamts.cpam33.ordonnance.domain.ports.in.patients;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.patients.RegisterPatientCmd;

public interface RegisterPatientPort {

    Patient registerPatient(RegisterPatientCmd registerPatientCmd);

}
