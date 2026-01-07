package fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.ExceptionCode;

public enum PatientExceptionCode implements ExceptionCode {

    BS_PATIENT_NOT_FOUND,
    BS_PATIENT_INVALID,
    BS_PATIENT_DATE_NAISSANCE_INVALID

}
