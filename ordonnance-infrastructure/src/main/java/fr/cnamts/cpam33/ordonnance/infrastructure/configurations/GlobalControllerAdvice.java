package fr.cnamts.cpam33.ordonnance.infrastructure.configurations;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorMessageDomainResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorMessageInfrastructureResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.ErrorDescriptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.errors.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerAdvice {

    private final static Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    public static final String INTERNAL_EXCEPTION = "INTERNAL_EXCEPTION";

    private final ErrorMessageDomainResolver errorMessageResolver;
    private final ErrorMessageInfrastructureResolver errorMessageInfrastructureResolver;

    public static final String DATE_NAISSANCE = "dateNaissance";
    public static final Map<String, String> errorMessages =  Map.of(
            "field", DATE_NAISSANCE, "message", "Le format de date attendu est yyyy-MM-dd"
    );

    public GlobalControllerAdvice(ErrorMessageDomainResolver errorMessageResolver,
                                  ErrorMessageInfrastructureResolver errorMessageInfrastructureResolver) {
        this.errorMessageResolver = errorMessageResolver;
        this.errorMessageInfrastructureResolver = errorMessageInfrastructureResolver;
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponseDto> handle(DomainException ex) {
        ErrorDescriptor descriptor = errorMessageResolver.resolve(ex.getCode(), ex.getPlaceHolders());
        logger.error("{} : {}", ex, descriptor);
        return ResponseEntity
                .status(descriptor.httpStatus())
                .body(ErrorResponseDto.from(descriptor));
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponseDto> handle(InfrastructureException ex) {
        ErrorDescriptor descriptor = ex.getPlaceHolders() == null
                ? errorMessageInfrastructureResolver.resolve(ex.getCode())
                : errorMessageInfrastructureResolver.resolve(ex.getCode(), ex.getPlaceHolders());
        logger.error("{} : {}", ex, descriptor);
        return ResponseEntity
                .status(descriptor.httpStatus())
                .body(ErrorResponseDto.from(descriptor));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handle(Exception ex) {
        ErrorDescriptor descriptor = new ErrorDescriptor(
                INTERNAL_EXCEPTION,
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                ex.getClass().getSimpleName()
        );
        return ResponseEntity.status(descriptor.httpStatus()).body(ErrorResponseDto.from(descriptor));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidDate(HttpMessageNotReadableException ex) {
        ResponseEntity<String> response = ResponseEntity.badRequest().body("Requête invalide");
        if ( ex.getCause() instanceof InvalidFormatException ife && ife.getTargetType().equals(LocalDate.class) ) {
            response = ResponseEntity.badRequest().body(errorMessages.get(DATE_NAISSANCE));
        }
        return response;
    }

}
