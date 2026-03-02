package fr.cnamts.cpam33.ordonnance.domain.ports.in.patients;

import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.ExternalPatientId;

public interface ImportPatientPort {

    Patient importerPatient(ExternalPatientId externalPatientId, UtilisateurId utilisateurId);

}
