package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.FonctionInvalidException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.FonctionExceptionCode;

public record Fonction(
        FonctionId fonctionId,
        String name
) implements DomainObject {

    public Fonction {
        if ( name == null || name.isEmpty() ) {
            throw new FonctionInvalidException(FonctionExceptionCode.BS_FONCTION_NAME_MISSING);
        }
    }

}
