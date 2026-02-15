package fr.cnamts.cpam33.ordonnance.domain.models.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums.ActeMetierExceptionCode;

public record ActeMetier(
        ActeMetierId acteMetierId,
        String objetMetierName,
        Fonction fonction
) implements DomainObject {

    public ActeMetier {
        if ( acteMetierId == null ) {
            throw new DomainException(ActeMetierExceptionCode.BS_ACTE_METIER_INVALID);
        }
        if ( objetMetierName == null || objetMetierName.isEmpty() ) {
            throw new DomainException(ActeMetierExceptionCode.BS_ACTE_METIER_OBJET_METIER_MISSING);
        }
        if ( fonction == null ) {
            throw new DomainException(ActeMetierExceptionCode.BS_ACTE_METIER_FONCTION_MISSING);
        }
    }

    @Override
    public String toString() {
        return "ActeMetier{" +
                "acteMetierId=" + acteMetierId +
                ", objetMetierName=" + objetMetierName +
                ", fonction=" + fonction +
                '}';
    }
}
