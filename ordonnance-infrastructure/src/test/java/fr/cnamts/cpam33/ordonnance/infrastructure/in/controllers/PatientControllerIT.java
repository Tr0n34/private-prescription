package fr.cnamts.cpam33.ordonnance.infrastructure.in.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.application.usecases.ProvidePatientUseCase;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorMessageDomainResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorMessageInfrastructureResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.ExternalPatientDtoFixtures;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.PatientController;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.ExternalPatientDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.PatientApiMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
@ActiveProfiles("integration")
public class PatientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProvidePatientUseCase patientService;

    @MockBean
    private PatientApiMapper patientApiMapper;

    @MockBean
    private ErrorMessageDomainResolver domainResolver;

    @MockBean
    private ErrorMessageInfrastructureResolver infrastructureResolver;

    @Test
    void should_create_patient_when_payload_is_valid() throws Exception {
        ExternalPatientDto patientDto = ExternalPatientDtoFixtures.patientValide1();
        when(patientApiMapper.toDomain(any())).thenReturn(PatientFixtures.patientValide());
        when(patientService.providePatient(any())).thenReturn(PatientFixtures.patientValide());
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isOk());
    }

    @Test
    void should_create_patient_batch_when_payload_is_valid() throws Exception {
        List<ExternalPatientDto> dtos = List.of(
                ExternalPatientDtoFixtures.patientValide1(),
                ExternalPatientDtoFixtures.patientValide2()
        );
        when(patientApiMapper.toDomain(any())).thenReturn(PatientFixtures.patientValide());
        when(patientService.providePatient(any())).thenReturn(PatientFixtures.patientValide());
        mockMvc.perform(post("/patients/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtos)))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_400_when_id_is_missing() throws Exception {
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ExternalPatientDtoFixtures.patientSansId())))
                .andExpect(status().isBadRequest());
    }

}
