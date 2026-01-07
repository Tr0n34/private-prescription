package fr.cnamts.cpam33.ordonnance.infrastructure.configurations;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainException;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.ErrorMessageDomainResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.ErrorMessageInfrastructureResolver;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.ErrorDescriptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.errors.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.Map;

@RestControllerAdvice
public class GlobalControllerAdvice {

    private final ErrorMessageDomainResolver errorMessageResolver;
    private final ErrorMessageInfrastructureResolver errorMessageInfrastructureResolver;

    public static final Map<String, String> errorMessages =  Map.of(
            "field", "dateNaissance", "message", "Le format de date attendu est yyyy-MM-dd"
    );

    public GlobalControllerAdvice(ErrorMessageDomainResolver errorMessageResolver,
                                  ErrorMessageInfrastructureResolver errorMessageInfrastructureResolver) {
        this.errorMessageResolver = errorMessageResolver;
        this.errorMessageInfrastructureResolver = errorMessageInfrastructureResolver;
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponseDto> handle(DomainException ex) {
        ErrorDescriptor descriptor = errorMessageResolver.resolve(ex.getCode());
        return ResponseEntity
                .status(descriptor.httpStatus())
                .body(ErrorResponseDto.from(descriptor));
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ErrorResponseDto> handle(InfrastructureException ex) {
        ErrorDescriptor descriptor = ex.getPlaceHolders() == null
                ? errorMessageInfrastructureResolver.resolve(ex.getCode())
                : errorMessageInfrastructureResolver.resolve(ex.getCode(), ex.getPlaceHolders());
        return ResponseEntity
                .status(descriptor.httpStatus())
                .body(ErrorResponseDto.from(descriptor));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidDate(HttpMessageNotReadableException ex) {
        if ( ex.getCause() instanceof InvalidFormatException ife
                && ife.getTargetType().equals(LocalDate.class) ) {
            return ResponseEntity.badRequest().body(
                errorMessages.get("dateNaissance")
            );
        }
        return ResponseEntity.badRequest().body("Requête invalide");
    }

}
