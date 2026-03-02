package fr.cnamts.cpam33.ordonnance.domain.ports.in.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.kernel.identifiants.OrdonnanceId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Signature;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.snapshots.OrdonnanceSnapshot;

public interface OrdonnanceSnapshotGenerator {

    OrdonnanceSnapshot snapshotOf(OrdonnanceId ordonnanceId, Signature signature);

}
