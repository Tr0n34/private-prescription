package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.snapshots;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.Document;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums.OrdonnanceExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Signature;

import java.time.LocalDateTime;

public record OrdonnanceSnapshot(
        Ordonnance ordonnance,
        Signature signature,
        LocalDateTime emmittedOn
) implements Document {

    public OrdonnanceSnapshot {
        if ( ordonnance == null ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_ID_MISSING);
        }
        if ( signature == null ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_SNAPSHOT_MUST_BE_SIGNED);
        }
    }

}
