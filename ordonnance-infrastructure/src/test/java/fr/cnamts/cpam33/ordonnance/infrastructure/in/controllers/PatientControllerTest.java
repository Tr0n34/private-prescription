package fr.cnamts.cpam33.ordonnance.infrastructure.in.controllers;

import fr.cnamts.cpam33.ordonnance.application.usecases.patients.RegisterPatientUseCase;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.acls.patients.PatientACL;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.routes.LocationBuilder;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.PatientController;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.ImportPatientApiMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.PatientApiMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.PatientDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PatientControllerTest {

    public static final String URL = "http://localhost/patients/";

    private LocationBuilder locationBuilder;
    private RegisterPatientUseCase patientService;
    private PatientController controller;
    private PatientACL patientACL;

    @BeforeEach
    void setup() {
        patientService = mock(RegisterPatientUseCase.class);
        locationBuilder = mock(LocationBuilder.class);
        patientACL = mock(PatientACL.class);
        controller = new PatientController(locationBuilder, patientService, patientACL);
    }

    @Test
    void should_create_patient() {
        var patientDomain = PatientFixtures.patientValideWithIdAndCes("1234567891234", "123");
        PatientDto dto = new PatientDto(patientDomain.patientId().numero(),
                patientDomain.externalPatientId().numero(),
                patientDomain.nom().value(),
                patientDomain.prenom().value(),
                LocalDate.of(1980, Month.SEPTEMBER, 5));
        URI fakeLocation = URI.create(URL + patientDomain.patientId().numero());
        when(locationBuilder.buildCreatedLocation(patientDomain.patientId().numero())).thenReturn(fakeLocation);
        when(patientService.registerPatient(patientDomain)).thenReturn(patientDomain);
        when(patientACL.toDomain(dto)).thenReturn(patientDomain);
        ResponseEntity<Void> result = controller.createPatient(dto);
        assertEquals(HttpStatus.CREATED.value(), result.getStatusCode().value());
        verify(patientService, times(1)).registerPatient(patientDomain);
    }

}
