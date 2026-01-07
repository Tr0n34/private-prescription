package fr.cnamts.cpam33.ordonnance.infrastructure.out.validators;

import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.errors.ErrorCatalogJsonDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class ErrorCatalogValidator {

    private final Validator validator;

    public ErrorCatalogValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(List<ErrorCatalogJsonDto> errors) {
        for ( ErrorCatalogJsonDto dto : errors ) {
            Set<ConstraintViolation<ErrorCatalogJsonDto>> violations = validator.validate(dto);
            if ( !violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        }
    }

}
