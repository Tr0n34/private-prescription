package fr.cnamts.cpam33.ordonnance.infrastructure.configurations;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorDescriptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.enums.ValidationDtoExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.resolvers.ErrorMessageDomainResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.resolvers.ErrorMessageInfrastructureResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.enums.TestExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.fixtures.enums.TestInfrastructureExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.errors.ErrorResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class GlobalControllerAdviceTest {

    private ErrorMessageDomainResolver domainResolver;
    private ErrorMessageInfrastructureResolver infraResolver;
    private GlobalControllerAdvice advice;

    @BeforeEach
    void setup() {
        domainResolver = mock(ErrorMessageDomainResolver.class);
        infraResolver = mock(ErrorMessageInfrastructureResolver.class);
        advice = new GlobalControllerAdvice(domainResolver, infraResolver);
    }

    @Test
    void should_handle_domain_exception_using_domain_resolver() {
        DomainException ex = mock(DomainException.class);
        Map<String, Object> placeholders = Map.of("k", "v");
        when(ex.getCode()).thenReturn(TestExceptionCode .CODE_ERREUR);
        when(ex.getPlaceHolders()).thenReturn(placeholders);
        ErrorDescriptor descriptor = new ErrorDescriptor(
                "CODE_ERREUR",
                "msg",
                BAD_REQUEST.value(),
                LocalDateTime.now(),
                "BC"
        );
        when(domainResolver.resolve(ex.getCode(), placeholders)).thenReturn(descriptor);
        var response = advice.handle(ex);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertErrorEqualsIgnoringTimestamp(ErrorResponseDto.from(descriptor), response.getBody());
        verify(domainResolver).resolve(ex.getCode(), placeholders);
        verifyNoInteractions(infraResolver);
    }

    @Test
    void should_handle_infrastructure_exception_without_placeholders() {
        InfrastructureException ex = mock(InfrastructureException.class);
        when(ex.getCode()).thenReturn(TestInfrastructureExceptionCode.CODE_INFRA_ERREUR);
        when(ex.getPlaceHolders()).thenReturn(null);
        ErrorDescriptor descriptor = new ErrorDescriptor(
                "CODE_INFRA_ERREUR",
                "msg",
                NOT_FOUND.value(),
                LocalDateTime.now(),
                "BC"
        );
        when(infraResolver.resolve(ex.getCode())).thenReturn(descriptor);
        var response = advice.handle(ex);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertErrorEqualsIgnoringTimestamp(ErrorResponseDto.from(descriptor), response.getBody());
        verify(infraResolver).resolve(ex.getCode());
        verifyNoMoreInteractions(infraResolver);
        verifyNoInteractions(domainResolver);
    }

    @Test
    void should_handle_infrastructure_exception_with_placeholders() {
        InfrastructureException ex = mock(InfrastructureException.class);
        Map<String, Object> placeholders = Map.of("a", 1);
        when(ex.getCode()).thenReturn(TestInfrastructureExceptionCode.CODE_INFRA_ERREUR);
        when(ex.getPlaceHolders()).thenReturn(placeholders);
        ErrorDescriptor descriptor = new ErrorDescriptor(
                "CODE_INFRA_ERREUR",
                "msg",
                CONFLICT.value(),
                LocalDateTime.now(),
                "BC"
        );
        when(infraResolver.resolve(ex.getCode(), placeholders)).thenReturn(descriptor);
        var response = advice.handle(ex);
        assertEquals(CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertErrorEqualsIgnoringTimestamp(ErrorResponseDto.from(descriptor), response.getBody());
        verify(infraResolver).resolve(ex.getCode(), placeholders);
        verifyNoMoreInteractions(infraResolver);
        verifyNoInteractions(domainResolver);
    }

    @Test
    void should_handle_method_argument_not_valid() throws Exception {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(new Object(), "createPatientRequestDto");
        br.addError(new FieldError("createPatientRequestDto", "externalId", "", false, null, null, "must not be blank"));

        Method m = DummyController.class.getDeclaredMethod("create", DummyDto.class);
        MethodParameter mp = new MethodParameter(m, 0);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(mp, br);
        ErrorDescriptor expected = new ErrorDescriptor(
                "BAD_REQUEST",
                "Requête invalide",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "API"
        );
        when(infraResolver.resolve(
                eq(ValidationDtoExceptionCode.TECH_API_VALIDATION_FAILED),
                anyMap()
        )).thenReturn(expected);
        var response = advice.handle(ex, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        var body = response.getBody();
        assertEquals("BAD_REQUEST", body.code());
        assertEquals("Requête invalide", body.message());
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.status());
        assertEquals("API", body.boundedContext());
    }

    static class DummyController {
        @SuppressWarnings("unused")
        public void create(DummyDto dto) {
            /* Dummy for stubing */
        }
    }
    static class DummyDto {}

    @Test
    void should_handle_generic_exception_as_internal() {
        Exception ex = new IllegalStateException("boom");
        var response = advice.handle(ex);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponseDto body = response.getBody();
        assertEquals(GlobalControllerAdvice.INTERNAL_EXCEPTION, body.code());
        assertEquals("boom", body.message());
        assertEquals(INTERNAL_SERVER_ERROR.value(), body.status());
        assertEquals("IllegalStateException", body.boundedContext());
    }

    @Test
    void should_handle_invalid_date_when_json_has_localdate_invalid_format() {
        InvalidFormatException ife = mock(InvalidFormatException.class);
        when(ife.getTargetType()).thenReturn((Class) LocalDate.class);
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("bad", ife);
        var response = advice.handleInvalidDate(ex);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals(GlobalControllerAdvice.errorMessages.get(GlobalControllerAdvice.DATE_NAISSANCE), response.getBody());
    }

    @Test
    void should_handle_invalid_date_default_message_when_not_localdate_format_issue() {
        InvalidFormatException ife = mock(InvalidFormatException.class);
        when(ife.getTargetType()).thenReturn((Class) Integer.class);
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("bad", ife);
        var response = advice.handleInvalidDate(ex);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Requête invalide", response.getBody());
    }

    @Test
    void should_handle_invalid_date_default_message_when_no_invalid_format_cause() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("bad", new RuntimeException("x"));
        var response = advice.handleInvalidDate(ex);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Requête invalide", response.getBody());
    }

    private void assertErrorEqualsIgnoringTimestamp(ErrorResponseDto expected, ErrorResponseDto actual) {
        assertEquals(expected.code(), actual.code());
        assertEquals(expected.message(), actual.message());
        assertEquals(expected.status(), actual.status());
        assertEquals(expected.boundedContext(), actual.boundedContext());
        assertNotNull(actual.timestamp());
    }

}

