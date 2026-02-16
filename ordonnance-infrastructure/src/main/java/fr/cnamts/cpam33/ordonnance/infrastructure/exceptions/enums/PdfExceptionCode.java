package fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.enums;

import fr.cnamts.cpam33.ordonnance.infrastructure.kernel.errors.InfraStructureExceptionCode;

public enum PdfExceptionCode implements InfraStructureExceptionCode {

    TECH_PDF_DIRECTORY_NOT_FOUND,
    TECH_PDF_READ_ERROR,
    TECH_PDF_WRITE_ERROR,
    TECH_PDF_DIRECTORY_CREATE_ERROR,
    TECH_PDF_KEY_INVALID,
    TECH_PDF_BASE64_EMPTY,
    TECH_PDF_PATH_INVALID,
    TECH_PDF_EMPTY,
    TECH_PDF_ROOT_DIR_EMPTY

}
