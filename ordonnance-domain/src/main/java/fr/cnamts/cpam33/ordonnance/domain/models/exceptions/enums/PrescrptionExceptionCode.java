package fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.ExceptionCode;

public enum PrescrptionExceptionCode implements ExceptionCode {

    BS_MEDICAMENT_MISSING,

    BS_POSOLOGIE_MISSING,
    BS_POSOLOGIE_PHRASE_MISSING,

    BS_PRESCRIPTION_ID_MISSING

}
