package fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.OrdonnanceId;

import java.util.List;

public interface ArchiverOrdonnancePort {

    void archiver(OrdonnanceId ordonnanceId);

    void archiver(List<OrdonnanceId> ordonnanceIds);

}
