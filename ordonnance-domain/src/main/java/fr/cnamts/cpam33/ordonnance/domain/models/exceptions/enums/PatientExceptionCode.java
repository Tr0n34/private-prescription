package fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.ExceptionCode;

public enum PatientExceptionCode implements ExceptionCode {

    BS_PATIENT_NOT_FOUND,
    BS_PATIENT_INVALID,
    BS_PATIENT_DATE_NAISSANCE_INVALID,

    BS_MALADIE_NOM_IS_MISSING

}
