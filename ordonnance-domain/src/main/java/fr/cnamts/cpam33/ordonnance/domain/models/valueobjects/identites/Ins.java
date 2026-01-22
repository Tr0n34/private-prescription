package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites;

import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.InsInvalidException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.PrenomInvalidException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.InsExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PrenomExceptionCode;

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
            throw new InsInvalidException(InsExceptionCode.BS_INS_MATRICULE_MISSING);
        }
        if ( prenoms == null || prenoms.isEmpty() ) {
            throw new PrenomInvalidException(PrenomExceptionCode.BS_PRENOM_MISSING);
        }
        prenoms = Collections.unmodifiableMap(prenoms);
    }

    public Prenom firstPrenom() {
        return prenoms.get(0);
    }

}
