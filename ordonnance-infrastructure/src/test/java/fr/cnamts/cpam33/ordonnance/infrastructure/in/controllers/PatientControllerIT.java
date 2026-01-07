package fr.cnamts.cpam33.ordonnance.infrastructure.in.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.application.usecases.ProvidePatientUseCase;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.ErrorMessageDomainResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.ErrorMessageInfrastructureResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.PatientController;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.patients.ExternalPatientDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.PatientApiMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
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
        ExternalPatientDto patientDto = new ExternalPatientDto("1234567891234",
                "Dupont", "Jean", LocalDate.of(1980, Month.SEPTEMBER, 5));
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
                new ExternalPatientDto("EXT-123", "Dupont", "Jean", LocalDate.of(1980, Month.SEPTEMBER, 5)),
                new ExternalPatientDto("EXT-456", "Martin", "Claire", LocalDate.of(1981, Month.SEPTEMBER, 6))
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
        ExternalPatientDto invalid = new ExternalPatientDto(null, "Dupont", "Jean",
                LocalDate.of(1980, Month.SEPTEMBER, 5));
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

}
