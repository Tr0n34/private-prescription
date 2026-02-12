package fr.cnamts.cpam33.ordonnance.infrastructure.in.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.application.usecases.patients.RegisterPatientUseCase;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.patients.RegisterPatientCmd;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        RegisterPatientCmd registerPatientCmd = new RegisterPatientCmd(
                patientDomain.externalPatientId(),
                patientDomain.nom(),
                patientDomain.prenom(),
                patientDomain.dateNaissance(),
                new UtilisateurId("123456")
        );

        when(patientACL.toDomain(any(PatientDto.class), anyString())).thenReturn(registerPatientCmd);
        when(registerPatientUseCase.registerPatient(registerPatientCmd)).thenReturn(patientDomain);

        URI location = URI.create("/patients/" + patientNumero);
        when(locationBuilder.buildCreatedLocation(eq(patientNumero))).thenReturn(location);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("userId", "123456")
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", location.toString()));

        verify(patientACL).toDomain(any(PatientDto.class), anyString());
        verify(registerPatientUseCase).registerPatient(registerPatientCmd);
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


}
