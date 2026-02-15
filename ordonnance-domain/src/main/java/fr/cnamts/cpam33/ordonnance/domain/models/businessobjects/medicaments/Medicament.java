package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Posologie;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums.MedecinExceptionCode;

import java.util.Collections;
import java.util.List;

public record Medicament(
        String nom,
        List<Posologie> posologies
) {

    public Medicament {
        if ( posologies == null ) {
            throw new DomainException(MedecinExceptionCode.BS_POSOLOGIE_PHRASE_MISSING);
        }
        posologies =  Collections.unmodifiableList(posologies);
    }

}
