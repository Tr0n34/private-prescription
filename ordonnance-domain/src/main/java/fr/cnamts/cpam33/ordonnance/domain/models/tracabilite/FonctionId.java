package fr.cnamts.cpam33.ordonnance.domain.models.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.DomainObjectId;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums.ActeMetierExceptionCode;

public record FonctionId(
        String code
) implements DomainObjectId {

    public FonctionId {
        if ( code == null || code.isEmpty() )
            throw new DomainException(ActeMetierExceptionCode.BS_FONCTION_NAME_MISSING);
    }

}
