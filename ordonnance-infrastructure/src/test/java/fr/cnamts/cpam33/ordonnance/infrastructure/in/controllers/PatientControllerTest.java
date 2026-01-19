package fr.cnamts.cpam33.ordonnance.infrastructure.in.controllers;

import fr.cnamts.cpam33.ordonnance.application.usecases.ProvidePatientUseCase;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.PatientController;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.ExternalPatientDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.PatientApiMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PatientControllerTest {

    private ProvidePatientUseCase patientService;
    private PatientApiMapper patientApiMapper;
    private PatientController controller;

    @BeforeEach
    void setup() {
        patientService = mock(ProvidePatientUseCase.class);
        patientApiMapper = mock(PatientApiMapper.class);
        controller = new PatientController(patientService, patientApiMapper);
    }

    @Test
    void should_create_patient() {
        var patientDomain = PatientFixtures.patientValideWithIdAndCes("1234567891234");
        ExternalPatientDto dto = new ExternalPatientDto(patientDomain.patientId().externalId(),
                patientDomain.nom().value(),
                patientDomain.prenom().value(),
                LocalDate.of(1980, Month.SEPTEMBER, 5));
        when(patientApiMapper.toDomain(dto)).thenReturn(patientDomain);
        when(patientService.providePatient(patientDomain)).thenReturn(patientDomain);
        ResponseEntity<Void> result = controller.providePatient(dto);
        assertEquals(200, result.getStatusCode().value());
        verify(patientApiMapper, times(1)).toDomain(dto);
        verify(patientService, times(1)).providePatient(patientDomain);
    }

}
