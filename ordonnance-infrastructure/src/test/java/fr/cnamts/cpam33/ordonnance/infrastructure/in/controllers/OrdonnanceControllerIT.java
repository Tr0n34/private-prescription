package fr.cnamts.cpam33.ordonnance.infrastructure.in.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.application.usecases.ordonnances.CreateOrdonnanceUseCase;
import fr.cnamts.cpam33.ordonnance.application.views.OrdonnanceView;
import fr.cnamts.cpam33.ordonnance.application.commands.ordonnances.CreateOrdonnanceCmd;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.resolvers.ErrorMessageDomainResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.resolvers.ErrorMessageInfrastructureResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces.OrdonnanceWebTraceConfiguration;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.OrdonnanceController;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.OrdonnanceDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.PrescriptionDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.medecins.MedecinIdDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.patients.PatientIdDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.interceptors.TraceHeaderInterceptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers.OrdonnanceDtoDomainMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = OrdonnanceController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        TraceHeaderInterceptor.class,
                        OrdonnanceWebTraceConfiguration.class
                }
        )
)
@ActiveProfiles("integration")
public class OrdonnanceControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateOrdonnanceUseCase createOrdonnanceUseCase;

    @MockBean
    private OrdonnanceDtoDomainMapper ordonnanceDtoDomainMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ErrorMessageDomainResolver domainResolver;

    @MockBean
    ErrorMessageInfrastructureResolver infrastructureResolver;

    @Test
    void createOrdonnance_shouldReturn201() throws Exception {
        OrdonnanceDto dto = new OrdonnanceDto(
                new PatientIdDto("1234567891234"),
                new MedecinIdDto("1234567893214", "12365478936"),
                List.of(new PrescriptionDto("test")));
        CreateOrdonnanceCmd cmd = Mockito.mock(CreateOrdonnanceCmd.class);
        OrdonnanceView ordonnance = Mockito.mock(OrdonnanceView.class);
        when(ordonnanceDtoDomainMapper.toCommand(Mockito.any(OrdonnanceDto.class))).thenReturn(cmd);
        when(createOrdonnanceUseCase.execute(cmd)).thenReturn(ordonnance);

        mockMvc.perform(post("/ordonnances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createOrdonnance_shouldReturn400_withInvalidDto() throws Exception {
        OrdonnanceDto invalidDto = new OrdonnanceDto(
                new PatientIdDto(""),
                new MedecinIdDto("", ""),
                List.of(new PrescriptionDto("test")));

        mockMvc.perform(post("/ordonnances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

}

