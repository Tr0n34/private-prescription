package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.ordonnances.PatientJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.PatientEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.patients.PatientEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientPersistenceAdapterTest {

    @Mock
    private PatientJpaRepository patientJpaRepository;

    @Mock
    private PatientEntityMapper patientEntityMapper;

    @InjectMocks
    private PatientPersistenceAdapter adapter;

    private PatientId patientId;
    private ExternalPatientId externalPatientId;
    private PatientEntity patientEntity;
    private Patient patient;

    @BeforeEach
    void setup() {
        patientId = new PatientId("P123");
        externalPatientId = new ExternalPatientId("EXT123");
        patientEntity = new PatientEntity()
                .setExternalId("EXT123")
                .setNom("Dupont")
                .setPrenom("Jean");
        patient = mock(Patient.class);
    }

    @Test
    void should_return_patient_when_findById_success() {
        when(patientJpaRepository.findByPatientId("P123")).thenReturn(Optional.of(patientEntity));
        when(patientEntityMapper.toDomain(patientEntity)).thenReturn(patient);
        Optional<Patient> result = adapter.findById(patientId);
        assertTrue(result.isPresent());
        assertEquals(patient, result.get());
        verify(patientJpaRepository).findByPatientId("P123");
        verify(patientEntityMapper).toDomain(patientEntity);
    }

    @Test
    void should_throw_exception_when_findById_not_found() {
        when(patientJpaRepository.findByPatientId("P123")).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> adapter.findById(patientId));
        verify(patientJpaRepository).findByPatientId("P123");
        verifyNoInteractions(patientEntityMapper);
    }

    @Test
    void should_save_patient() {
        when(patientEntityMapper.toEntity(patient)).thenReturn(patientEntity);
        when(patientJpaRepository.save(patientEntity)).thenReturn(patientEntity);
        when(patientEntityMapper.toDomain(patientEntity)).thenReturn(patient);
        Patient result = adapter.save(patient);
        assertEquals(patient, result);
        verify(patientEntityMapper).toEntity(patient);
        verify(patientJpaRepository).save(patientEntity);
        verify(patientEntityMapper).toDomain(patientEntity);
    }

    @Test
    void should_return_patient_when_findByExternalIdOrThrow_success() {
        when(patientJpaRepository.findByExternalId("EXT123")).thenReturn(Optional.of(patientEntity));
        when(patientEntityMapper.toDomain(patientEntity)).thenReturn(patient);
        Optional<Patient> result = adapter.findByExternalIdOrThrow(externalPatientId);
        assertTrue(result.isPresent());
        assertEquals(patient, result.get());
    }

    @Test
    void should_throw_exception_when_findByExternalIdOrThrow_not_found() {
        when(patientJpaRepository.findByExternalId("EXT123")).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> adapter.findByExternalIdOrThrow(externalPatientId));
    }

    @Test
    void should_return_patient_when_findByExternalId_success() {
        when(patientJpaRepository.findByExternalId("EXT123")).thenReturn(Optional.of(patientEntity));
        when(patientEntityMapper.toDomain(patientEntity)).thenReturn(patient);
        Optional<Patient> result = adapter.findByExternalId(externalPatientId);
        assertTrue(result.isPresent());
        assertEquals(patient, result.get());
    }

    @Test
    void should_return_empty_when_findByExternalId_not_found() {
        when(patientJpaRepository.findByExternalId("EXT123")).thenReturn(Optional.empty());
        Optional<Patient> result = adapter.findByExternalId(externalPatientId);
        assertTrue(result.isEmpty());
        verifyNoInteractions(patientEntityMapper);
    }

}
