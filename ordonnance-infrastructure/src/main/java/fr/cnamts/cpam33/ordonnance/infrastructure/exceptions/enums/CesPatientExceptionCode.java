package fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.enums;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.ExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.InfraStructureExceptionCode;

public enum CesPatientExceptionCode implements InfraStructureExceptionCode {

    TECH_CES_PATIENT_ID_NOT_LENGTH,
    TECH_CES_PATIENT_ID_CES_RATTACHEMENT_INVALID

}
