package fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.ExceptionCode;

public enum TraceExceptionCode implements ExceptionCode {

    BS_TRACE_ID_MALFORMED,
    BS_TRACE_ACTE_METIER_MISSING,
    BS_TRACE_TIMESTAMP_MISSING,
    BS_TRACE_TIMESTAMP_INVALID,
    BS_TRACE_MEDECIN_MISSING;

}
