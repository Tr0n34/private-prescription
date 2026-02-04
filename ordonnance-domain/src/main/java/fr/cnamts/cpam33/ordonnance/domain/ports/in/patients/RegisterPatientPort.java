package fr.cnamts.cpam33.ordonnance.domain.ports.in.patients;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;

public interface RegisterPatientPort {

    Patient registerPatient(Patient patient);

}
