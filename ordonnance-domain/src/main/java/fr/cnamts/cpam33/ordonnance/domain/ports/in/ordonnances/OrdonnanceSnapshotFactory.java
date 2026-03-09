package fr.cnamts.cpam33.ordonnance.domain.ports.in.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.OrdonnanceExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Signature;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.snapshots.OrdonnanceSnapshot;

public class OrdonnanceSnapshotFactory {

    private OrdonnanceSnapshotFactory() {
        throw new IllegalStateException( "Cannot instantiate class " + OrdonnanceSnapshotFactory.class.getName() );
    }

    public static OrdonnanceSnapshot create(Ordonnance ordonnance, Signature signature) {
        if ( !ordonnance.isSigned() ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_ALREADY_SIGNED);
        }
        return new OrdonnanceSnapshot(ordonnance, signature, ordonnance.signedOn());
    }

}
