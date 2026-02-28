package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums.PatientExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.ordonnances.PatientJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.patients.PatientEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
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
    public Optional<Patient> findById(PatientId patientId) {
        return Optional.of(patientEntityMapper.toDomain(patientJpaRepository.findByPatientId(patientId.numero())
                .orElseThrow(() -> new DomainException(PatientExceptionCode.BS_PATIENT_NOT_FOUND))));
    }

    @Override
    public List<Patient> findAll() {
        return List.of();
    }

    @Override
    public Patient save(Patient patient) {
        return patientEntityMapper.toDomain(patientJpaRepository.save(patientEntityMapper.toEntity(patient))
        );
    }

    @Override
    public Optional<Patient> findByExternalIdOrThrow(ExternalPatientId externalPatientId) {
        return Optional.of(patientEntityMapper.toDomain(patientJpaRepository.findByExternalId(externalPatientId.numero())
                .orElseThrow(() -> new DomainException(PatientExceptionCode.BS_PATIENT_NOT_FOUND))));
    }

    @Override
    public Optional<Patient> findByExternalId(ExternalPatientId externalPatientId) {
        return patientJpaRepository.findByExternalId(externalPatientId.numero())
                .map(patientEntityMapper::toDomain);
    }

}
