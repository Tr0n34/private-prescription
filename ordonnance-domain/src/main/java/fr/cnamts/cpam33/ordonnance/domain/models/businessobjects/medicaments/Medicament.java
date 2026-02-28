package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Posologie;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.MedicamentId;

import java.util.Collections;
import java.util.List;

public record Medicament(
        MedicamentId medicamentId,
        String nom,
        String nomLong,
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
        posologies =  Collections.unmodifiableList(posologies);
    }

}
