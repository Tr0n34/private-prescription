package fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.ExceptionCode;

public enum ActeMetierExceptionCode implements ExceptionCode {

    BS_ACTE_METIER_FONCTION_MISSING,
    BS_ACTE_METIER_OBJET_METIER_MISSING,
    BS_ACTE_METIER_INVALID,

    BS_FONCTION_NAME_MISSING;

}
