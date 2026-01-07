package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public record Ins(
        String matricule,
        String oid,
        Nom nom,
        List<Prenom> prenoms,
        LocalDate dateNaissance,
        String codeCommuneNaissance
) {

    public Ins {
        if ( matricule == null || matricule.isEmpty() ) {
            throw new IllegalArgumentException( "matricule cannot be null or empty" );
        }
        prenoms = Collections.unmodifiableList(prenoms);
    }

}
