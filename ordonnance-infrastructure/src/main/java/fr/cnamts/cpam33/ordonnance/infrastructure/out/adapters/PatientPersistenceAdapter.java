package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObjectNotFound;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PatientExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances.PatientEntityMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.ordonnances.PatientJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PatientPersistenceAdapter implements PatientRepository, Adapter {

    private final PatientJpaRepository patientJpaRepository;
    private final PatientEntityMapper patientEntityMapper;

    public PatientPersistenceAdapter(PatientJpaRepository patientJpaRepository,
                                     PatientEntityMapper patientEntityMapper) {
        this.patientJpaRepository = patientJpaRepository;
        this.patientEntityMapper = patientEntityMapper;
    }

    @Override
    public Optional<Patient> findById(PatientId patientId) throws DomainObjectNotFound {
        return Optional.of(patientEntityMapper.toDomain(patientJpaRepository.findByExternalId(patientId.externalId()).orElseThrow(
                () -> new DomainObjectNotFound(PatientExceptionCode.BS_PATIENT_NOT_FOUND)
        )));
    }

    @Override
    public Patient save(Patient patient) {
        return patientEntityMapper.toDomain(patientJpaRepository.save(patientEntityMapper.toEntity(patient))
        );
    }

}
