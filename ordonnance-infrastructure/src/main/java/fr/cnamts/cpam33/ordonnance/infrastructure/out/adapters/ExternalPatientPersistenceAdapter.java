package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.ExternalPatientDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExternalPatientPersistenceAdapter implements Adapter {

    ExternalPatientApiClient externalPatientApiClient;

    public ExternalPatientPersistenceAdapter(ExternalPatientApiClient externalPatientApiClient) {
        this.externalPatientApiClient = externalPatientApiClient;
    }

    public Patient fetchById(PatientId patientId) {
        ExternalPatientDto patientDto = externalPatientApiClient.fetchPatient(patientId.externalId());
        return new Patient(
                new PatientId(patientDto.externalId()),
                new Nom(patientDto.nom()),
                new Prenom(patientDto.prenom()),
                patientDto.dateNaissance()
        );
    }

    public List<Patient> fetchByIds(List<PatientId> patientIds) {
        return patientIds.stream()
                .map(this::fetchById)
                .toList();
    }

}
