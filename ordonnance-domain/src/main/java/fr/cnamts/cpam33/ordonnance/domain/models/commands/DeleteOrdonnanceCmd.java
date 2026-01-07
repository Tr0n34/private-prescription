package fr.cnamts.cpam33.ordonnance.domain.models.commands;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.Command;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.OrdonnanceId;

public record DeleteOrdonnanceCmd(
        OrdonnanceId ordonnanceId
) implements Command {

    public DeleteOrdonnanceCmd {
        if ( ordonnanceId == null ) {
            throw new IllegalArgumentException( "OrdonnanceId is null" );
        }
    }

}
