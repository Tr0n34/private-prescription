package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.listeners;

import fr.cnamts.cpam33.ordonnance.application.usecases.ProvidePatientUseCase;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;

import java.io.File;

public class PatientListener implements Adapter {

    // Objet de connexion au système de fichier
    ProvidePatientUseCase patientService;

    public PatientListener(ProvidePatientUseCase patientService) {
        this.patientService = patientService;
    }

    public File getPatientFile(Patient patient) {
        // traitier le fichier ligne par ligne et appeler le service patient (application
        patientService.providePatient(patient);
        // créer ficher temoin file_machin.ok
        return null;
    }

}
