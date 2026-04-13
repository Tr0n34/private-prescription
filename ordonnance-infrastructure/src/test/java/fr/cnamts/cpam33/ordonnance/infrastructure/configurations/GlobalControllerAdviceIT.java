package fr.cnamts.cpam33.ordonnance.infrastructure.configurations;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorDescriptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.resolvers.ErrorMessageDomainResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.resolvers.ErrorMessageInfrastructureResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces.OrdonnanceWebTraceConfiguration;
import fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.controllers.TestController;
import fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.controllers.ThrowingService;
import fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.enums.TestExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.enums.TestInfrastructureExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.interceptors.TraceHeaderInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = TestController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        TraceHeaderInterceptor.class,
                        OrdonnanceWebTraceConfiguration.class
                    }
        )
)
@Import(GlobalControllerAdvice.class)
public class GlobalControllerAdviceIT {

    @Autowired
    MockMvc mvc;

    @MockBean
    ErrorMessageDomainResolver errorMessageResolver;
    @MockBean
    ErrorMessageInfrastructureResolver errorMessageInfrastructureResolver;

    @MockBean
    ThrowingService service;

    @Test
    void should_handle_generic_exception_in_web_layer() throws Exception {
        doThrow(new IllegalStateException("boom")).when(service).boom();
        mvc.perform(get("/test/boom"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(GlobalControllerAdvice.INTERNAL_EXCEPTION))
                .andExpect(jsonPath("$.message").value("boom"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.boundedContext").value("IllegalStateException"));
        verify(service).boom();
    }

    @Test
    void should_handle_domain_exception_in_web_layer() throws Exception {
        DomainException ex = mock(DomainException.class);
        Map<String, Object> ph = Map.of("k", "v");
        when(ex.getCode()).thenReturn(TestExceptionCode.CODE_ERREUR);
        when(ex.getPlaceHolders()).thenReturn(ph);
        ErrorDescriptor descriptor = new ErrorDescriptor(
                "CODE_ERREUR", "msg", 400, LocalDateTime.now(), "BC"
        );
        when(errorMessageResolver.resolve(ex.getCode(), ph)).thenReturn(descriptor);
        doThrow(ex).when(service).domain();
        mvc.perform(get("/test/domain"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("CODE_ERREUR"))
                .andExpect(jsonPath("$.message").value("msg"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.boundedContext").value("BC"));
        verify(service).domain();
        verify(errorMessageResolver).resolve(ex.getCode(), ph);
        verifyNoInteractions(errorMessageInfrastructureResolver);
    }

    @Test
    void should_handle_infrastructure_exception_in_web_layer_without_placeholders() throws Exception {
        InfrastructureException ex = mock(InfrastructureException.class);
        when(ex.getCode()).thenReturn(TestInfrastructureExceptionCode.CODE_INFRA_ERREUR);
        when(ex.getPlaceHolders()).thenReturn(null);
        ErrorDescriptor descriptor = new ErrorDescriptor(
                "CODE_INFRA_ERREUR", "msg", 404, LocalDateTime.now(), "BC"
        );
        when(errorMessageInfrastructureResolver.resolve(ex.getCode())).thenReturn(descriptor);
        doThrow(ex).when(service).infra();
        mvc.perform(get("/test/infra"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("CODE_INFRA_ERREUR"))
                .andExpect(jsonPath("$.message").value("msg"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.boundedContext").value("BC"));

        verify(service).infra();
        verify(errorMessageInfrastructureResolver).resolve(ex.getCode());
        verifyNoInteractions(errorMessageResolver);
    }

}


