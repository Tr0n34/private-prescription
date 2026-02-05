package fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.enums;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfraStructureExceptionCode;

public enum VaultExceptionCode implements InfraStructureExceptionCode {

    TECH_VAULT_KV_NOT_READY,
    TECH_VAULT_KV_READ_PATH_EMPTY

}
