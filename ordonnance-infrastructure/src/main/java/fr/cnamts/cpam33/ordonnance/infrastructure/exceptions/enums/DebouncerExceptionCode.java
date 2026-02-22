package fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.enums;

import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.InfraStructureExceptionCode;

public enum DebouncerExceptionCode implements InfraStructureExceptionCode {

    TECH_PENDING_TASK_KEY_NOT_NULL,
    TECH_PENDING_TASK_ACTION_NOT_NULL,
    TECH_PENDING_TASK_DELAY_POSITIVE


}
