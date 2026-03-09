package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors;

import fr.cnamts.cpam33.ordonnance.application.exceptions.InvalidErrorDescriptorException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;

import java.time.LocalDateTime;

public record ErrorDescriptor(
        String code,
        String message,
        int httpStatus,
        LocalDateTime timestamp,
        String boundedContext
) implements DomainObject {

    public ErrorDescriptor {
        if ( code == null || code.isEmpty() ) {
            throw new InvalidErrorDescriptorException("Le code ne peut pas être vide");
        }
        if ( httpStatus < 400  ) {
            throw new InvalidErrorDescriptorException("Le code HTTP n'est pas un code d'erreur");
        }
        if ( message == null || message.isEmpty() ) {
            throw new InvalidErrorDescriptorException("Le message d'erreurs ne peut pas être vide. Mauvaise configuration.");
        }
        if ( boundedContext == null || boundedContext.isEmpty() ) {
            throw new InvalidErrorDescriptorException("L'objet métier ne peut pas être vide. Mauvaise configuration.");
        }
    }

    public static ErrorDescriptor of(String code, String message, int httpStatus, LocalDateTime timestamp, String boundedContext) {
        return new ErrorDescriptor(code, message, httpStatus, timestamp, boundedContext);
    }

}
