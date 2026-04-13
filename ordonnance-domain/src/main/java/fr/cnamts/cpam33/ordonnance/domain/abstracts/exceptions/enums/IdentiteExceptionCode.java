package fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.ExceptionCode;

public enum IdentiteExceptionCode implements ExceptionCode {

    BS_INS_OID_MISSING,
    BS_INS_MATRICULE_MISSING,

    BS_PRENOMS_MISSING,
    BS_PRENOM_LENGTH,

    BS_NOM_MISSING,
    BS_NOM_LENGTH


}
