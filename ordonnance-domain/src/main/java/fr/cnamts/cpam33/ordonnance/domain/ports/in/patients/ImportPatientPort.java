package fr.cnamts.cpam33.ordonnance.domain.ports.in.patients;

import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;

public interface ImportPatientPort {

    Patient importerPatient(ExternalPatientId externalPatientId, UtilisateurId utilisateurId);

}
