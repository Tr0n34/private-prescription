package fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.snapshots.OrdonnanceSnapshot;

public interface OrdonnanceSnapshotWriter {

    void writeSnapshot(OrdonnanceSnapshot snapshot);

}
