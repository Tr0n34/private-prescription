package fr.cnamts.cpam33.ordonnance.infrastructure.configurations;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public final class DtoValidationExceptionHandler {

    private DtoValidationExceptionHandler() {
        throw new NotImplementedException("Utility class");
    }

    public static String formatFieldError(FieldError fe) {
        // ex: "externalId: must not be blank"
        // defaultMessage dépend de ton MessageSource / i18n Bean Validation
        String msg = fe.getDefaultMessage();
        if (msg == null || msg.isBlank()) msg = "invalid";
        return fe.getField() + ": " + msg;
    }

    public static String formatGlobalError(ObjectError oe) {
        String msg = oe.getDefaultMessage();
        if (msg == null || msg.isBlank()) msg = "invalid";
        return oe.getObjectName() + ": " + msg;
    }

    public static String concat(List<String> a, List<String> b) {
        return List.of(a, b).stream()
                .flatMap(List::stream)
                .collect(Collectors.joining("; "));
    }

}
