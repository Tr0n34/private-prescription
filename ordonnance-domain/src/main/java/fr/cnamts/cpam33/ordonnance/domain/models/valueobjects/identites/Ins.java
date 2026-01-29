package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites;

import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.IdentiteExceptionCode;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

public record Ins(
        String matricule,
        String oid,
        Nom nom,
        Map<Integer, Prenom> prenoms,
        LocalDate dateNaissance,
        String codeCommuneNaissance
) {

    public Ins {
        if ( matricule == null || matricule.isEmpty() ) {
            throw new DomainException(IdentiteExceptionCode.BS_INS_MATRICULE_MISSING);
        }
        if ( prenoms == null || prenoms.isEmpty() ) {
            throw new DomainException(IdentiteExceptionCode.BS_PRENOM_MISSING);
        }
        prenoms = Collections.unmodifiableMap(prenoms);
    }

    public Prenom firstPrenom() {
        return prenoms.get(0);
    }

}
