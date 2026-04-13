package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObjectId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.ActeMetierExceptionCode;

public record FonctionId(
        String code
) implements DomainObjectId {

    public FonctionId {
        if ( code == null || code.isEmpty() )
            throw new DomainException(ActeMetierExceptionCode.BS_FONCTION_NAME_MISSING);
    }

}
