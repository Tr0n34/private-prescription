package fr.cnamts.cpam33.ordonnance.infrastructure.configurations;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.BoundedContextHint;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.ErrorDescriptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.enums.ValidationDtoExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.resolvers.ErrorMessageDomainResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.resolvers.ErrorMessageInfrastructureResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.errors.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static fr.cnamts.cpam33.ordonnance.infrastructure.configurations.DtoValidationExceptionHandler.concat;

@RestControllerAdvice
public class GlobalControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    public static final String INTERNAL_EXCEPTION = "INTERNAL_EXCEPTION";
    public static final String ERROR_STANDARD_LOG = "{} : {}";
    public static final String BOUNDED_CONTEXT_FROM_API_ERROR = "API_ERROR";

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
        logger.error(ERROR_STANDARD_LOG, ex, descriptor);
        return ResponseEntity
                .status(descriptor.httpStatus())
                .body(ErrorResponseDto.from(descriptor));
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponseDto> handle(InfrastructureException ex) {
        ErrorDescriptor descriptor = ex.getPlaceHolders() == null
                ? errorMessageInfrastructureResolver.resolve(ex.getCode())
                : errorMessageInfrastructureResolver.resolve(ex.getCode(), ex.getPlaceHolders());
        logger.error(ERROR_STANDARD_LOG, ex, descriptor);
        return ResponseEntity
                .status(descriptor.httpStatus())
                .body(ErrorResponseDto.from(descriptor));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handle(MethodArgumentNotValidException ex, HandlerMethod handlerMethod) {
        String boundedContext = BOUNDED_CONTEXT_FROM_API_ERROR;
        if ( handlerMethod != null ) {
            BoundedContextHint annotation = handlerMethod.getBeanType().getAnnotation(BoundedContextHint.class);
            if ( annotation != null && !annotation.value().isBlank() ) {
                boundedContext = annotation.value();
            }
        }
        String violations = buildViolationsMessage(ex);
        ErrorDescriptor descriptor = errorMessageInfrastructureResolver.resolve(
                ValidationDtoExceptionCode.TECH_API_VALIDATION_FAILED,
                Map.of(
                        "violations", violations,
                        "violationsCount", ex.getBindingResult().getErrorCount(),
                        "boundedContext", boundedContext
                ));
        return ResponseEntity.status(descriptor.httpStatus()).body(ErrorResponseDto.from(descriptor));
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
        logger.error(ERROR_STANDARD_LOG, ex, descriptor);
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

    private static String buildViolationsMessage(MethodArgumentNotValidException ex) {
        List<String> fieldParts = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DtoValidationExceptionHandler::formatFieldError)
                .toList();
        List<String> globalParts = ex.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(DtoValidationExceptionHandler::formatGlobalError)
                .toList();

        return concat(fieldParts, globalParts);
    }


}
