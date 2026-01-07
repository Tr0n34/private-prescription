package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.snapshots.OrdonnanceSnapshot;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.views.LigneOrdonnanceView;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.views.OrdonnancePdfView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrdonnancePdfViewMapper {

    public static final String SPACE_BETWEEN_NOM_PRENOM = " ";

    public static OrdonnancePdfView from(OrdonnanceSnapshot snapshot) {
        List<LigneOrdonnanceView> lignes =
                snapshot.ordonnance().prescriptions().stream()
                        .map(ligne -> new LigneOrdonnanceView(
                                ligne.medicament().name(),
                                ligne.posologie().phrase()
                        ))
                        .toList();
        return new OrdonnancePdfView(
                snapshot.ordonnance().patient().nom().value()
                        + SPACE_BETWEEN_NOM_PRENOM
                        + snapshot.ordonnance().patient().prenom().value(),
                snapshot.ordonnance().patient().dateNaissance().format(DateTimeFormatter.ISO_LOCAL_DATE),
                snapshot.ordonnance().medecin().nom().value(),
                lignes
        );
    }
}
