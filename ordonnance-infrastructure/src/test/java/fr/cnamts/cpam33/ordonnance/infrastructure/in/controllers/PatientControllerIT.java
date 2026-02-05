package fr.cnamts.cpam33.ordonnance.infrastructure.in.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.application.usecases.patients.RegisterPatientUseCase;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorMessageDomainResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorMessageInfrastructureResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.GlobalControllerAdvice;
import fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.ExternalPatientDtoFixtures;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.acls.patients.PatientACL;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.PatientController;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.routes.LocationBuilder;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.PatientDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@ActiveProfiles("integration")
@Import(GlobalControllerAdvice.class)
class PatientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterPatientUseCase registerPatientUseCase;

    @MockBean
    private PatientACL patientACL;

    @MockBean
    private LocationBuilder locationBuilder;

    // Garde-les si ton contexte MVC les demande (ControllerAdvice, etc.)
    @MockBean
    private ErrorMessageDomainResolver domainResolver;

    @MockBean
    private ErrorMessageInfrastructureResolver infrastructureResolver;

    @Test
    void should_create_patient_when_payload_is_valid() throws Exception {
        PatientDto patientDto = ExternalPatientDtoFixtures.patientValide1();
        Patient patientDomain = PatientFixtures.patientValide();
        String patientNumero = patientDomain.patientId().numero();

        when(patientACL.toDomain(any(PatientDto.class))).thenReturn(patientDomain);
        when(registerPatientUseCase.registerPatient(any(Patient.class))).thenReturn(patientDomain);

        URI location = URI.create("/patients/" + patientNumero);
        when(locationBuilder.buildCreatedLocation(eq(patientNumero))).thenReturn(location);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", location.toString()));

        verify(patientACL).toDomain(any(PatientDto.class));
        verify(registerPatientUseCase).registerPatient(any(Patient.class));
        verify(locationBuilder).buildCreatedLocation(eq(patientNumero));
        verifyNoMoreInteractions(patientACL, registerPatientUseCase, locationBuilder);
    }

    @Test
    void should_return_400_when_payload_is_invalid() throws Exception {
        PatientDto invalidDto = ExternalPatientDtoFixtures.patientInvalid();
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(patientACL, registerPatientUseCase, locationBuilder);
    }

    @Test
    void should_create_patient_batch_when_payload_is_valid() throws Exception {
        // Arrange
        List<PatientDto> dtos = List.of(
                ExternalPatientDtoFixtures.patientValide1(),
                ExternalPatientDtoFixtures.patientValide2()
        );

        Patient patientDomain = PatientFixtures.patientValide();
        when(patientACL.toDomain(any(PatientDto.class))).thenReturn(patientDomain);
        when(registerPatientUseCase.registerPatient(any(Patient.class))).thenReturn(patientDomain);

        // Act + Assert
        mockMvc.perform(post("/patients/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtos)))
                .andExpect(status().isOk());

        verify(patientACL, times(dtos.size())).toDomain(any(PatientDto.class));
        verify(registerPatientUseCase, times(dtos.size())).registerPatient(any(Patient.class));
        verifyNoInteractions(locationBuilder);
    }
/*
    @Test
    void should_import_from_external_when_payload_is_valid() throws Exception {
        CesPatientDto cesDto = ExternalPatientDtoFixtures.cesPatientValide();
        when(patientACL.toDomain(any(CesPatientDto.class))).thenReturn(PatientFixtures.patientValide());

        // Act + Assert
        mockMvc.perform(post("/patients/import")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cesDto)))
                .andExpect(status().isOk());

        verify(patientACL).toDomain(any(CesPatientDto.class));
        verifyNoInteractions(registerPatientUseCase, locationBuilder);
    }
 */

}
