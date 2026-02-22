package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums.MedecinExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Posologie;

import java.util.Collections;
import java.util.List;

public record Medicament(
        MedicamentId medicamentId,
        String nom,
        String cdfNom,
        String catcCode,
        String cipUcd,
        Forme forme,
        String atu,
        String t2a,
        VoieAdministration voieAdministration,
        List<Posologie> posologies
) implements DomainObject {

    public Medicament {
        if ( posologies == null ) {
            throw new DomainException(MedecinExceptionCode.BS_POSOLOGIE_PHRASE_MISSING);
        }
        posologies =  Collections.unmodifiableList(posologies);
    }

}
