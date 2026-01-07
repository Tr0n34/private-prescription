package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.PosologieInvalidException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PosologieExceptionCode;

public record Posologie(
        String phrase
) implements DomainObject {

    public Posologie {
        if ( phrase == null || phrase.isEmpty() ) {
            throw new PosologieInvalidException(PosologieExceptionCode.BS_POSOLOGIE_PHRASE_MISSING);
        }
    }

    public static Posologie of(String phrase) {
        return new Posologie(phrase);
    }

}
