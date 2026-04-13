package fr.cnamts.cpam33.ordonnance.application.commands;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.ErrorPlaceHolders;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.CommandCodeException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidatableCommand;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidationResult;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.Validator;

import java.util.Map;

public final class CommandValidation {

    private static final String COMMAND = "command";
    private static final String MISSING_PARAMS = "missingParams";

    private CommandValidation() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void failFast(String commandName, ValidationResult result) {
        if ( result != null && !result.errors().isEmpty() ) {
            throw new DomainException(
                    CommandCodeException.CMD_PARAMETRE_MANQUANT,
                    ErrorPlaceHolders.of(
                            COMMAND, commandName,
                            MISSING_PARAMS, toJsonArray(result.errors())
                    )
            );
        }
    }

    public static void ensureValid(String commandName, ValidatableCommand cmd) {
        failFast(commandName, cmd.validate());
    }

    public static void ensureValid(ValidatableCommand cmd) {
        ensureValid(cmd.getClass().getSimpleName(), cmd);
    }

    public static ValidationResult notNullArgs(Map<String, Object> args) {
        Validator validator = new Validator();
        args.forEach((name, value) -> validator.notNull(value, name));
        return validator.validate();
    }

    private static String toJsonArray(Map<String, Object> invalid) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for ( String k : invalid.keySet() ) {
            if ( !first ) sb.append(',');
            first = false;
            sb.append('"').append(escapeJson(k)).append('"');
        }
        sb.append(']');
        return sb.toString();
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}
