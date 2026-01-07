package fr.cnamts.cpam33.ordonnance.application.usecases;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.ports.in.patients.ProviderPatientPort;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class ProvidePatientUseCase implements ProviderPatientPort {

    private final PatientRepository patientRepository;

    public ProvidePatientUseCase(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient providePatient(Patient patient) {
        return patientRepository.save(patient);
    }

}
