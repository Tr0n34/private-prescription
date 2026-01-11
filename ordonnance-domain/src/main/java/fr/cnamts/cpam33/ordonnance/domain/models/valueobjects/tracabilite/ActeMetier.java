package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.ActeMetierInvalidException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.ActeMetierExceptionCode;

public record ActeMetier(
        ActeMetierId acteMetierId,
        String objetMetierName,
        Fonction fonction
) implements DomainObject {

    public ActeMetier {
        if ( acteMetierId == null ) {
            throw new ActeMetierInvalidException(ActeMetierExceptionCode.BS_ACTE_METIER_INVALID);
        }
        if ( objetMetierName == null || objetMetierName.isEmpty() ) {
            throw new ActeMetierInvalidException(ActeMetierExceptionCode.BS_ACTE_METIER_OBJET_METIER_MISSING);
        }
        if ( fonction == null ) {
            throw new ActeMetierInvalidException(ActeMetierExceptionCode.BS_ACTE_METIER_FONCTION_MISSING);
        }
    }

}
