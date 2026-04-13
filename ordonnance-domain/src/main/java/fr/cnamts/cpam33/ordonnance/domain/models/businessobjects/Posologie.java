package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.MedecinExceptionCode;

public record Posologie(
        String phrase
) implements DomainObject {

    public Posologie {
        if ( phrase == null || phrase.isEmpty() ) {
            throw new DomainException(MedecinExceptionCode.BS_POSOLOGIE_PHRASE_MISSING);
        }
    }

    public static Posologie of(String phrase) {
        return new Posologie(phrase);
    }

}
