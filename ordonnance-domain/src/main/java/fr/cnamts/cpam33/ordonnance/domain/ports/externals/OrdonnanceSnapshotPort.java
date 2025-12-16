package fr.cnamts.cpam33.ordonnance.domain.ports.externals;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.OrdonnanceId;

public interface OrdonnanceSnapshotPort {

    void generate(OrdonnanceId ordonnanceId);

}
