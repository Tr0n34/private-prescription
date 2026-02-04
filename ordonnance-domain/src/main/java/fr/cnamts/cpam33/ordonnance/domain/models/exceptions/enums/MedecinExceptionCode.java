package fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.ExceptionCode;

public enum MedecinExceptionCode implements ExceptionCode {

    BS_MEDECIN_NOT_FOUND,
    BS_MEDECIN_INVALID,

    BS_RPPS_MISSING,
    BS_RPPS_LENGTH,

    BS_POSOLOGIE_PHRASE_MISSING

}
