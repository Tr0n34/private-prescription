package fr.cnamts.cpam33.ordonnance.domain.ports.out.patients;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.PatientId;

public interface PatientNumGenerator {

    String generate();

    PatientId newpatientId(String externalId);

}
