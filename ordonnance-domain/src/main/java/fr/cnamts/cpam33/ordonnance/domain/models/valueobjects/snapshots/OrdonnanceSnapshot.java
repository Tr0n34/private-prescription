package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.snapshots;

import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.OrdonnanceInvalideException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.OrdonnanceExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Signature;

import java.time.LocalDateTime;

public record OrdonnanceSnapshot(
        Ordonnance ordonnance,
        Signature signature,
        LocalDateTime emmittedOn
) {

    public OrdonnanceSnapshot {
        if ( ordonnance == null ) {
            throw new OrdonnanceInvalideException(OrdonnanceExceptionCode.BS_ORDONNANCE_ID_MISSING);
        }
        if ( signature == null ) {
            throw new OrdonnanceInvalideException(OrdonnanceExceptionCode.BS_ORDONNANCE_SNAPSHOT_MUST_BE_SIGNED);
        }
    }

}
