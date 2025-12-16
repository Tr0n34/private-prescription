package fr.cnamts.cpam33.ordonnance.domain.ports.internals;

import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.OrdonnanceVersion;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.IdentiteMedecin;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.IdentitePatient;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.OrdonnanceId;

public class OrdonnanceIdFactory {

    private final OrdonnanceNumGenerator numGenerator;

    private OrdonnanceIdFactory(OrdonnanceNumGenerator numGenerator) {
        this.numGenerator = numGenerator;
    }

    public OrdonnanceId create(
            IdentiteMedecin medecin,
            IdentitePatient patient) {
        return new OrdonnanceId(
                numGenerator.generate(),
                medecin.rpps(),
                OrdonnanceVersion.first()
        );
    }

}
