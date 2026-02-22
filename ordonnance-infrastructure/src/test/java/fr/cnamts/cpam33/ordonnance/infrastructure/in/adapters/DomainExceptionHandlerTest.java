package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums.OrdonnanceExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.GlobalControllerAdvice;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.ErrorDescriptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.errors.ErrorResponseDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.ErrorMessageDomainResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DomainExceptionHandlerTest {

    @Mock
    ErrorMessageDomainResolver resolver;

    @InjectMocks
    GlobalControllerAdvice globalControllerAdvice;

    @Test
    void should_map_domain_exception_to_error_response_dto() {
        ErrorDescriptor descriptor = new ErrorDescriptor(
                "BS_ORDONNANCE_IMMUTABLE_WHEN_SIGNED",
                "Une ordonnance signée ne peut plus être modifiée",
                409,
                LocalDateTime.now(),
                "Ordonnance"
        );
        when(resolver.resolve(any(), any())).thenReturn(descriptor);
        DomainException exception = new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_IMMUTABLE_WHEN_SIGNED, null);
        ResponseEntity<ErrorResponseDto> response = globalControllerAdvice.handle(exception);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(409));
        ErrorResponseDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.code()).isEqualTo("BS_ORDONNANCE_IMMUTABLE_WHEN_SIGNED");
        assertThat(body.message()).isEqualTo("Une ordonnance signée ne peut plus être modifiée");
        assertThat(body.status()).isEqualTo(409);
        verify(resolver).resolve(OrdonnanceExceptionCode.BS_ORDONNANCE_IMMUTABLE_WHEN_SIGNED, null);
        verifyNoMoreInteractions(resolver);
    }

}
