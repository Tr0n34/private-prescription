package fr.cnamts.cpam33.ordonnance.domain.models.commands;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.Command;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.OrdonnanceId;
import fr.cnamts.cpam33.ordonnance.domain.models.events.TraceCommand;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.MedecinId;

public record DeleteOrdonnanceCmd(
        OrdonnanceId ordonnanceId,
        MedecinId medecinId
) implements TraceCommand {

    public DeleteOrdonnanceCmd {
        if ( ordonnanceId == null ) {
            throw new IllegalArgumentException( "OrdonnanceId is null" );
        }
    }

    @Override
    public MedecinId medecinId() {
        return medecinId;
    }

}
