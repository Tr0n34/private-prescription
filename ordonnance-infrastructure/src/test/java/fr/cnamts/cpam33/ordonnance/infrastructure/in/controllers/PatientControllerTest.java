package fr.cnamts.cpam33.ordonnance.infrastructure.in.controllers;

import fr.cnamts.cpam33.ordonnance.application.usecases.patients.ImportPatientUseCase;
import fr.cnamts.cpam33.ordonnance.application.usecases.patients.RegisterPatientUseCase;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.acls.patients.PatientACL;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.PatientController;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.routes.LocationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PatientControllerTest {

    public static final String URL = "http://localhost/patients/";

    private LocationBuilder locationBuilder;
    private RegisterPatientUseCase registerPatientUseCase;
    private ImportPatientUseCase importPatientUseCase;
    private PatientController controller;
    private PatientACL patientACL;

    @BeforeEach
    void setup() {
        registerPatientUseCase = mock(RegisterPatientUseCase.class);
        locationBuilder = mock(LocationBuilder.class);
        patientACL = mock(PatientACL.class);
        importPatientUseCase = mock(ImportPatientUseCase.class);
        controller = new PatientController(locationBuilder, registerPatientUseCase, importPatientUseCase, patientACL);
    }

    @Test
    void should_create_patient() {

    }

}
