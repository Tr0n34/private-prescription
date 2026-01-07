package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.notifications;

import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.AlerteInvalidException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.AlerteExceptionCode;

import java.time.Instant;

public record Alerte(
        String code,
        String message,
        TypeAlerte typeAlerte,
        Criticite criticite,
        Instant emission
) {

    public Alerte {
        if ( code == null || code.isEmpty() ) {
            throw new AlerteInvalidException(AlerteExceptionCode.BS_ALERTE_CODE_INVALIDE);
        }
        if ( message == null || message.isEmpty() ) {
            throw new AlerteInvalidException(AlerteExceptionCode.BS_ALERTE_MESSAGE_INVALIDE);
        }
    }

}
