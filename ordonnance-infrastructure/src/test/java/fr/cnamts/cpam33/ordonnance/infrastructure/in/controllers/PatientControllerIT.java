package fr.cnamts.cpam33.ordonnance.infrastructure.in.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.application.usecases.patients.ImportPatientUseCase;
import fr.cnamts.cpam33.ordonnance.application.usecases.patients.RegisterPatientUseCase;
import fr.cnamts.cpam33.ordonnance.application.views.PatientView;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.application.commands.patients.RegisterPatientCmd;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.resolvers.ErrorMessageDomainResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.resolvers.ErrorMessageInfrastructureResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.GlobalControllerAdvice;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces.OrdonnanceWebTraceConfiguration;
import fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.ExternalPatientDtoFixtures;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.acls.patients.PatientACL;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.PatientController;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.routes.LocationBuilder;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.interceptors.TraceHeaderInterceptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.patients.PatientDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = PatientController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        TraceHeaderInterceptor.class,
                        OrdonnanceWebTraceConfiguration.class
                }
        )
)
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
    private ImportPatientUseCase importPatientUseCase;

    @MockBean
    private PatientACL patientACL;

    @MockBean
    private LocationBuilder locationBuilder;

    @MockBean
    private ErrorMessageDomainResolver domainResolver;

    @MockBean
    private ErrorMessageInfrastructureResolver infrastructureResolver;

    @Test
    void should_create_patient_when_payload_is_valid() throws Exception {
        PatientDto patientDto = ExternalPatientDtoFixtures.patientValide1();
        PatientView patientView = new PatientView(
                "123456789",
                "987654321",
                "Dupont",
                "Jean",
                LocalDate.of(1985, 9, 5)
        );
        RegisterPatientCmd registerPatientCmd = new RegisterPatientCmd(
                new ExternalPatientId("987654321"),
                new Nom("Dupont"),
                new Prenom("Jean"),
                LocalDate.of(1985, 9, 5),
                new UtilisateurId("123456")
        );

        when(patientACL.toCommand(any(PatientDto.class), anyString())).thenReturn(registerPatientCmd);
        when(registerPatientUseCase.registerPatient(registerPatientCmd)).thenReturn(patientView);

        URI location = URI.create("/patients/" + patientView.patientId());
        when(locationBuilder.buildCreatedLocation(eq(patientView.patientId()))).thenReturn(location);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("userId", "123456")
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", location.toString()));

        verify(patientACL).toCommand(any(PatientDto.class), anyString());
        verify(registerPatientUseCase).registerPatient(registerPatientCmd);
        verify(locationBuilder).buildCreatedLocation(eq(patientView.patientId()));
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
