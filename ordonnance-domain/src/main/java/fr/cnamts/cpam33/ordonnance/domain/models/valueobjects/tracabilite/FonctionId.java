package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObjectId;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.FonctionInvalidException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.FonctionExceptionCode;

public record FonctionId(
        String code
) implements DomainObjectId {

    public FonctionId {
        if ( code == null || code.isEmpty() )
            throw new FonctionInvalidException(FonctionExceptionCode.BS_FONCTION_NAME_MISSING);
    }

}
