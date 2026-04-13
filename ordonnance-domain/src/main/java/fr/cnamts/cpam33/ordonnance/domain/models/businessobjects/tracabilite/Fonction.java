package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.ActeMetierExceptionCode;

public record Fonction(
        FonctionId fonctionId,
        String description
) implements DomainObject {

    public Fonction {
        if ( fonctionId == null ) {
            throw new DomainException(ActeMetierExceptionCode.BS_FONCTION_NAME_MISSING);
        }
        if ( description == null || description.isEmpty() ) {
            throw new DomainException(ActeMetierExceptionCode.BS_FONCTION_NAME_MISSING);
        }
    }

}
