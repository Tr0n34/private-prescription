package fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.ExceptionCode;

public enum PrescrptionExceptionCode implements ExceptionCode {

    BS_MEDICAMENT_MISSING,

    BS_POSOLOGIE_MISSING,
    BS_POSOLOGIE_PHRASE_MISSING,

    BS_PRESCRIPTION_ID_MISSING

}
