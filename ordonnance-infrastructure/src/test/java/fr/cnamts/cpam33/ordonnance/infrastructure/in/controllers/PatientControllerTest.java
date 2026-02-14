package fr.cnamts.cpam33.ordonnance.infrastructure.in.controllers;

import fr.cnamts.cpam33.ordonnance.application.usecases.patients.RegisterPatientUseCase;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.patients.RegisterPatientCmd;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.acls.patients.PatientACL;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.PatientController;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.routes.LocationBuilder;
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
    private RegisterPatientUseCase registerPatientUseCase;
    private PatientController controller;
    private PatientACL patientACL;

    @BeforeEach
    void setup() {
        registerPatientUseCase = mock(RegisterPatientUseCase.class);
        locationBuilder = mock(LocationBuilder.class);
        patientACL = mock(PatientACL.class);
        controller = new PatientController(locationBuilder, registerPatientUseCase, patientACL);
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
        RegisterPatientCmd registerPatientCmd = new RegisterPatientCmd(
                patientDomain.externalPatientId(),
                patientDomain.nom(),
                patientDomain.prenom(),
                patientDomain.dateNaissance(),
                new UtilisateurId("123456")
        );
        when(patientACL.toDomain(dto, "123456")).thenReturn(registerPatientCmd);
        when(registerPatientUseCase.registerPatient(registerPatientCmd)).thenReturn(patientDomain);
        ResponseEntity<Void> result = controller.createPatient(dto, "123456");
        assertEquals(HttpStatus.CREATED.value(), result.getStatusCode().value());
        verify(registerPatientUseCase, times(1)).registerPatient(registerPatientCmd);
    }

}
